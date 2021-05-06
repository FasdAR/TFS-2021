package ru.fasdev.tfs.data.old.repo

import androidx.room.RoomDatabase
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable.fromIterable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.fasdev.tfs.data.old.mapper.mapToDomain
import ru.fasdev.tfs.data.old.source.db.dao.MessageDao
import ru.fasdev.tfs.data.old.source.db.dao.ReactionDao
import ru.fasdev.tfs.data.old.source.db.dao.UserDao
import ru.fasdev.tfs.data.old.source.db.model.MessageDB
import ru.fasdev.tfs.data.old.source.db.model.ReactionDB
import ru.fasdev.tfs.data.old.source.db.model.UserDB
import ru.fasdev.tfs.data.old.source.network.base.response.Result
import ru.fasdev.tfs.data.old.source.network.chat.api.ChatApi
import ru.fasdev.tfs.data.old.source.network.chat.model.FilterNarrow
import ru.fasdev.tfs.domain.old.message.model.Message
import ru.fasdev.tfs.domain.old.message.model.Reaction
import ru.fasdev.tfs.domain.old.message.repo.MessageRepo
import ru.fasdev.tfs.domain.old.user.model.User

class MessageRepoImpl(
    private val chatApi: ChatApi,
    private val json: Json,
    private val roomDatabase: RoomDatabase,
    private val messageDao: MessageDao,
    private val userDao: UserDao,
    private val reactionDao: ReactionDao
) : MessageRepo {
    companion object {
        private const val ANCHOR_NEWEST = "newest"
        const val NEWEST_ANCHOR = -1L

        private const val OPERATOR_STREAM = "stream"
        private const val OPERATOR_TOPIC = "topic"

        const val USER_ID = 402233L
    }

    override fun getMessagesByTopic(
        nameStream: String,
        nameTopic: String,
        anchorMessage: Long,
        limit: Int,
        afterCount: Int,
        beforeCount: Int,
        isFirstLoad: Boolean
    ): Flowable<List<Message>> {
        val filterNarrowStream = FilterNarrow(operator = OPERATOR_STREAM, operand = nameStream)
        val filterNarrowTopic = FilterNarrow(operator = OPERATOR_TOPIC, operand = nameTopic)
        val narrowJson = json.encodeToString(listOf(filterNarrowStream, filterNarrowTopic))
        val currentAnchorMessage = if (anchorMessage != NEWEST_ANCHOR) {
            anchorMessage.toString()
        } else {
            ANCHOR_NEWEST
        }

        val isActualData: Boolean = anchorMessage == NEWEST_ANCHOR

        val networkSource = chatApi.getAllMessages(
            anchor = currentAnchorMessage,
            numAfter = afterCount, numBefore = beforeCount,
            narrow = narrowJson
        )
            .map { it.messages }
            .flatMapObservable(::fromIterable)
            .map { it.mapToDomain(USER_ID) }
            .doOnNext{ message ->
                val sizeTable = messageDao.getDataCount(nameTopic)
                if (isActualData || sizeTable < 50) {
                    val reactionsDB = message.reactions.map { reaction ->
                        ReactionDB(idMessage = message.id, emoji = reaction.emoji,
                            emojiName = reaction.emojiName, countSelection = reaction.countSelection,
                            isSelected = reaction.isSelected)
                    }

                    val userDB = UserDB(id = message.sender.id, avatarUrl = message.sender.avatarUrl,
                        fullName = message.sender.fullName, email = message.sender.email)

                    val messageDB = MessageDB(message.id, message.sender.id, nameTopic, message.text, message.date)

                    roomDatabase.runInTransaction {
                        userDao.insert(userDB)
                        messageDao.insert(messageDB)
                        if (reactionsDB.isNotEmpty()) reactionDao.insert(reactionsDB)
                    }
                }

                if (sizeTable > 50) {
                    val differ = sizeTable - 50
                    messageDao.deleteLastRow(differ)
                }
            }
            .toList()

        val dbSource = messageDao.getAllByTopic(nameTopic)
            .flatMapObservable(::fromIterable)
            .map { message ->
                val sender = User(
                    id = message.sender.id, avatarUrl = message.sender.avatarUrl,
                    fullName = message.sender.fullName, email = message.sender.email
                )

                val reactions = message.reactions.map { reaction ->
                    Reaction(
                        emoji = reaction.emoji,
                        emojiName = reaction.emojiName,
                        countSelection = reaction.countSelection,
                        isSelected = reaction.isSelected
                    )
                }

                Message(
                    id = message.messageDB.id,
                    sender = sender,
                    text = message.messageDB.text,
                    date = message.messageDB.date,
                    reactions = reactions
                )
            }
            .toList()

        return if (isFirstLoad && isActualData) dbSource.concatWith(networkSource) else networkSource.toFlowable()
    }

    override fun sendMessage(nameStream: String, nameTopic: String, message: String): Completable {
        return chatApi.sendMessage(to = nameStream, subject = nameTopic, content = message)
            .flatMapCompletable { Completable.fromCallable { it.result == Result.SUCCESS } }
    }

    override fun addEmoji(messageId: Int, emojiName: String): Completable {
        return chatApi.addReaction(messageId, emojiName)
            .flatMapCompletable { Completable.fromCallable { it.result == Result.SUCCESS } }
    }

    override fun removeEmoji(messageId: Int, emojiName: String): Completable {
        return chatApi.removeReaction(messageId, emojiName)
            .flatMapCompletable { Completable.fromCallable { it.result == Result.SUCCESS } }
    }
}
