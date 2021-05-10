package ru.fasdev.tfs.data.newPck.repository.messages

import io.reactivex.Completable
import io.reactivex.Observable
import ru.fasdev.tfs.domain.newPck.message.model.Message

interface MessagesRepository {
    fun getMessagesPage(
        nameStream: String,
        nameTopic: String,
        idAnchorMessage: Long?,
        afterMessageCount: Int,
        beforeMessageCount: Int
    ) : Observable<List<Message>>

    fun sendMessage(nameStream: String, nameTopic: String, message: String): Completable
}