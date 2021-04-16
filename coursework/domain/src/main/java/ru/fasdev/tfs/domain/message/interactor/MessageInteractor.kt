package ru.fasdev.tfs.domain.message.interactor

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import ru.fasdev.tfs.domain.message.model.DirectionScroll
import ru.fasdev.tfs.domain.message.model.Message

interface MessageInteractor {
    fun getMessagesByTopic(nameStream: String, nameTopic: String, anchorMessage: Long, direction: DirectionScroll, isFirstLoaded: Boolean = false): Flowable<List<Message>>
    fun getMessagesByTopic(nameStream: String, nameTopic: String, anchorMessage: Long, numBefore: Int = 0, numAfter: Int = 0, isFirstLoaded: Boolean = false): Flowable<List<Message>>
    fun sendMessage(nameStream: String, nameTopic: String, message: String): Completable
    fun setSelectionReaction(messageId: Int, emojiName: String, isSelected: Boolean): Completable
}
