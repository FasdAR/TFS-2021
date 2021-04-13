package ru.fasdev.tfs.domain.message.interactor

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.fasdev.tfs.domain.message.model.Message
import ru.fasdev.tfs.domain.message.repo.MessageRepo

class MessageInteractorImpl(private val messageRepo: MessageRepo) : MessageInteractor {
    override fun getMessagesByTopic(nameStream: String, nameTopic: String, anchorMessage: Long, direction: Int): Single<List<Message>> {
        return messageRepo.getMessagesByTopic(nameStream, nameTopic, anchorMessage, 20, direction)
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
