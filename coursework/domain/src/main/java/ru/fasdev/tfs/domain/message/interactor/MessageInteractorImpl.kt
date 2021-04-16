package ru.fasdev.tfs.domain.message.interactor

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import ru.fasdev.tfs.domain.message.model.DirectionScroll
import ru.fasdev.tfs.domain.message.model.Message
import ru.fasdev.tfs.domain.message.repo.MessageRepo

class MessageInteractorImpl(private val messageRepo: MessageRepo) : MessageInteractor {
    private companion object {
        const val LIMIT_PAGE = 20
    }

    override fun getMessagesByTopic(
        nameStream: String,
        nameTopic: String,
        anchorMessage: Long,
        direction: DirectionScroll,
        isFirstLoaded: Boolean
    ): Flowable<List<Message>> {
        val isAfterDirection = direction == DirectionScroll.DOWN
        val afterCount = if (isAfterDirection) LIMIT_PAGE else 0
        val beforeCount = if (!isAfterDirection) LIMIT_PAGE else 0

        return getMessagesByTopic(nameStream, nameTopic, anchorMessage, beforeCount, afterCount, isFirstLoaded)
    }

    override fun getMessagesByTopic(
        nameStream: String,
        nameTopic: String,
        anchorMessage: Long,
        numBefore: Int,
        numAfter: Int,
        isFirstLoaded: Boolean
    ): Flowable<List<Message>> {
        return messageRepo.getMessagesByTopic(nameStream, nameTopic, anchorMessage,
            LIMIT_PAGE, numAfter, numBefore, isFirstLoaded)
    }

    override fun sendMessage(nameStream: String, nameTopic: String, message: String): Completable {
        return messageRepo.sendMessage(nameStream, nameTopic, message)
    }

    override fun setSelectionReaction(
        messageId: Int,
        emojiName: String,
        isSelected: Boolean
    ): Completable {
        return if (isSelected) messageRepo.addEmoji(messageId, emojiName)
        else messageRepo.removeEmoji(messageId, emojiName)
    }
}
