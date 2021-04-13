package ru.fasdev.tfs.domain.message.interactor

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.fasdev.tfs.domain.message.model.Message

interface MessageInteractor {
    fun getMessagesByTopic(nameStream: String, nameTopic: String, anchorMessage: Long, direction: Int): Single<List<Message>>
    fun sendMessage(nameStream: String, nameTopic: String, message: String): Completable
    fun setSelectionReaction(messageId: Int, emojiName: String, isSelected: Boolean): Completable
}
