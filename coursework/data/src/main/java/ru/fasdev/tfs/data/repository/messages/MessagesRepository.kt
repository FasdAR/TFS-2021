package ru.fasdev.tfs.data.repository.messages

import io.reactivex.Completable
import io.reactivex.Observable
import ru.fasdev.tfs.domain.message.model.Message

interface MessagesRepository {
    fun getMessagesPage(
        nameStream: String,
        nameTopic: String,
        idStream: Long,
        idAnchorMessage: Long?,
        afterMessageCount: Int,
        beforeMessageCount: Int
    ): Observable<List<Message>>

    fun sendMessage(nameStream: String, nameTopic: String, message: String): Completable

    fun addReaction(messageId: Long, emojiName: String): Completable
    fun removeReaction(messageId: Long, emojiName: String): Completable

    fun listenUpdate(nameStream: String, nameTopic: String): Observable<List<Message>>
}
