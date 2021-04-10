package ru.fasdev.tfs.data.repo

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable.fromIterable
import io.reactivex.rxjava3.core.Single
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.fasdev.tfs.data.mapper.mapToDomain
import ru.fasdev.tfs.data.source.network.chat.api.ChatApi
import ru.fasdev.tfs.data.source.network.chat.model.FilterNarrow
import ru.fasdev.tfs.domain.message.model.Message
import ru.fasdev.tfs.domain.message.repo.MessageRepo
import java.util.*

class MessageRepoImpl(private val chatApi: ChatApi, private val json: Json) : MessageRepo
{
    override fun getMessagesByTopic(nameStream: String, nameTopic: String): Single<List<Message>> {
        val filterNarrow = FilterNarrow(operator = "stream", operand = nameStream)

        return chatApi.getAllMessages(narrow = json.encodeToString(filterNarrow))
            .map { it.messages }
            .flatMapObservable(::fromIterable)
            .filter { it.subject.toLowerCase(Locale.ROOT).equals(nameTopic.toLowerCase(Locale.ROOT)) }
            .map {  it.mapToDomain() }
            .toList()
    }

    override fun sendMessage(nameStream: String, nameTopic: String, message: String): Completable {
        return Completable.fromCallable {
            chatApi.sendMessage(to = nameStream, subject = nameTopic, content = message)
        }
    }
}