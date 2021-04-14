package ru.fasdev.tfs.data.repo

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable.fromIterable
import io.reactivex.rxjava3.core.Single
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.fasdev.tfs.data.mapper.mapToDomain
import ru.fasdev.tfs.data.source.network.base.response.Result
import ru.fasdev.tfs.data.source.network.chat.api.ChatApi
import ru.fasdev.tfs.data.source.network.chat.model.FilterNarrow
import ru.fasdev.tfs.domain.message.model.Message
import ru.fasdev.tfs.domain.message.repo.MessageRepo
import java.util.Locale

class MessageRepoImpl(private val chatApi: ChatApi, private val json: Json) : MessageRepo {
    companion object {
        private const val ANCHOR_NEWEST = "newest"
        const val NULL_ANCHOR = -1L

        private const val OPERATOR_STREAM = "stream"
        private const val OPERATOR_TOPIC = "topic"

        const val DIRECTION_BEFORE = -1
        const val DIRECTION_AFTER = 1

        const val USER_ID = 402233L
    }

    override fun getMessagesByTopic(
        nameStream: String, nameTopic: String, anchorMessage: Long, limit: Int, direction: Int
    ): Single<List<Message>> {
        val filterNarrowStream = FilterNarrow(operator = OPERATOR_STREAM, operand = nameStream)
        val filterNarrowTopic = FilterNarrow(operator = OPERATOR_TOPIC, operand = nameTopic)
        val narrowJson = json.encodeToString(listOf(filterNarrowStream, filterNarrowTopic))
        val currentAnchorMessage = if (anchorMessage != NULL_ANCHOR) anchorMessage.toString() else ANCHOR_NEWEST

        val isBeforeDirection = direction == DIRECTION_BEFORE
        val afterCount = if (!isBeforeDirection) limit else 0 // ¯\_(ツ)_/¯ Так же лучше чем растить скобки, если помещается по длине
        val beforeCount = if (isBeforeDirection) limit else 0

        return chatApi.getAllMessages(
            anchor = currentAnchorMessage,
            numAfter = afterCount, numBefore = beforeCount,
            narrow = narrowJson
        )
            .map { it.messages }
            .flatMapObservable(::fromIterable)
            .map { it.mapToDomain(USER_ID) }
            .toList()
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
