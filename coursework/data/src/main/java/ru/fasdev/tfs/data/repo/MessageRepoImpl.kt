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
        private const val USER_ID = 402233L
    }

    override fun getMessagesByTopic(nameStream: String, nameTopic: String): Single<List<Message>> {
        val filterNarrow = FilterNarrow(operator = "stream", operand = nameStream)

        return chatApi.getAllMessages(narrow = json.encodeToString(listOf(filterNarrow)))
            .map { it.messages }
            .flatMapObservable(::fromIterable)
            .filter { it.subject.toLowerCase(Locale.ROOT).equals(nameTopic.toLowerCase(Locale.ROOT)) }
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
