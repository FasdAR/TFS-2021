package ru.fasdev.tfs.domain.old.message.interactor

import io.reactivex.Completable
import io.reactivex.Flowable
import ru.fasdev.tfs.domain.old.message.model.DirectionScroll
import ru.fasdev.tfs.domain.old.message.model.Message

interface MessageInteractor {
    fun getMessagesByTopic(nameStream: String, nameTopic: String, anchorMessage: Long, direction: DirectionScroll, isFirstLoaded: Boolean = false): Flowable<List<Message>>
    fun getMessagesByTopic(nameStream: String, nameTopic: String, anchorMessage: Long, numBefore: Int = 0, numAfter: Int = 0, isFirstLoaded: Boolean = false): Flowable<List<Message>>
    fun sendMessage(nameStream: String, nameTopic: String, message: String): Completable
    fun setSelectionReaction(messageId: Int, emojiName: String, isSelected: Boolean): Completable
}
