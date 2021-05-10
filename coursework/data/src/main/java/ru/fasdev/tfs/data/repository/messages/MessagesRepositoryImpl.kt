package ru.fasdev.tfs.data.repository.messages

import io.reactivex.Completable
import io.reactivex.Observable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.fasdev.tfs.data.mapper.*
import ru.fasdev.tfs.data.source.database.dao.MessageDao
import ru.fasdev.tfs.data.source.database.dao.ReactionDao
import ru.fasdev.tfs.data.source.database.dao.UserDao
import ru.fasdev.tfs.data.source.network.base.model.ZulipResult
import ru.fasdev.tfs.data.source.network.messages.api.MessagesApi
import ru.fasdev.tfs.data.source.network.base.model.Narrow
import ru.fasdev.tfs.data.source.network.events.manager.EventsManager
import ru.fasdev.tfs.data.source.network.events.model.EventType
import ru.fasdev.tfs.data.source.network.events.model.Operation
import ru.fasdev.tfs.domain.message.model.Message

class MessagesRepositoryImpl(
    private val json: Json,
    private val messagesApi: MessagesApi,
    private val eventsManager: EventsManager,
    private val userDao: UserDao,
    private val messageDao: MessageDao,
    private val reactionDao: ReactionDao
) : MessagesRepository {
    companion object {
        private const val USER_ID = 402233L
        private const val ANCHOR_NEWEST = "newest"

        private const val OPERATOR_STREAM = "stream"
        private const val OPERATOR_TOPIC = "topic"
    }

    override fun getMessagesPage(
        nameStream: String,
        nameTopic: String,
        idAnchorMessage: Long?,
        afterMessageCount: Int,
        beforeMessageCount: Int
    ): Observable<List<Message>> {
        val narrowStream = Narrow(operator = OPERATOR_STREAM, operand = nameStream)
        val narrowTopic = Narrow(operator = OPERATOR_TOPIC, operand = nameTopic)
        val narrowJson = json.encodeToString(listOf(narrowStream, narrowTopic))

        val anchorMessage: String = idAnchorMessage?.toString() ?: ANCHOR_NEWEST

        return messagesApi.getMessages(
            anchor = anchorMessage,
            numBefore = beforeMessageCount,
            numAfter = afterMessageCount,
            narrow = narrowJson
        )
            .map { it.messages }
            .flatMapObservable {
                Observable.fromIterable(it)
                    .map { it.toMessageDomain(USER_ID) }
                    .toSortedList { item1, item2 -> item1.date.compareTo(item2.date) }
                    .toObservable()
            }
            .flatMap {
                val users = it.map { it.sender }.map { it.toUserDb() }
                val messages = it.map { it.toMessageDb() }
                val reactions = it.flatMap { message ->
                    message.reactions.map { reaction ->
                        reaction.toReactionDb(message.id)
                    }
                }

                userDao.insertReplace(users)
                    .andThen(messageDao.insertReplace(messages))
                    .andThen(reactionDao.insertReplace(reactions))
                    .andThen(Observable.just(it))
            }
    }

    override fun sendMessage(nameStream: String, nameTopic: String, message: String): Completable {
        return messagesApi.sendMessage(
            to = nameStream,
            subject = nameTopic,
            content = message
        ).flatMapCompletable {
            Completable.fromCallable { it.result == ZulipResult.SUCCESS }
        }
    }

    override fun addReaction(messageId: Long, emojiName: String): Completable {
        return messagesApi.addReaction(messageId, emojiName)
            .flatMapCompletable { Completable.fromCallable { it.result == ZulipResult.SUCCESS } }
    }

    override fun removeReaction(messageId: Long, emojiName: String): Completable {
        return messagesApi.deleteReaction(messageId, emojiName)
            .flatMapCompletable { Completable.fromCallable { it.result == ZulipResult.SUCCESS } }
    }

    override fun listenUpdate(nameStream: String, nameTopic: String): Observable<List<Message>> {
        val narrowStream = Narrow(operator = OPERATOR_STREAM, operand = nameStream)
        val narrowTopic = Narrow(operator = OPERATOR_TOPIC, operand = nameTopic)
        val narrows = listOf(narrowStream, narrowTopic)

        val eventTypes = listOf(EventType.MESSAGE, EventType.REACTION)

        return eventsManager.startListen(narrows, eventTypes)
            .flatMap {
                Observable.fromIterable(it)
                    .flatMap { event ->
                        when (event.type) {
                            EventType.MESSAGE -> {
                                val msg = event.toMessage().toMessageDomain(USER_ID)

                                messageDao.insertReplace(msg.toMessageDb())
                                    .andThen(Observable.just(msg))
                            }
                            EventType.REACTION -> {
                                reactionDao.getByReactionByKey(event.messageId!!, event.emojiName!!)
                                    .map {
                                        when (event.op) {
                                            Operation.ADD -> {
                                                it.copy(countSelection = it.countSelection + 1, isSelected = true)
                                            }
                                            Operation.REMOVE -> {
                                                it.copy(countSelection = it.countSelection - 1, isSelected = false)
                                            }
                                            null -> it
                                        }
                                    }
                                    .toObservable()
                                    .flatMap {
                                        reactionDao.insertReplace(it)
                                            .andThen(
                                                messageDao.getMessageById(event.messageId)
                                                .map { it.toMessageDomain() }
                                                .toObservable()
                                            )
                                    }
                            }
                        }
                    }
                    .toSortedList { item1, item2 -> item1.date.compareTo(item2.date) }
                    .toObservable()
            }
    }
}