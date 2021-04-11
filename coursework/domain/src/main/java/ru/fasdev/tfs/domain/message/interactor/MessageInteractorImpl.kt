package ru.fasdev.tfs.domain.message.interactor

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.fasdev.tfs.domain.message.repo.MessageRepo
import ru.fasdev.tfs.domain.message.model.Message

class MessageInteractorImpl(private val messageRepo: MessageRepo) : MessageInteractor {
    override fun getMessagesByTopic(nameStream: String, nameTopic: String): Single<List<Message>> {
        return messageRepo.getMessagesByTopic(nameStream, nameTopic)
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
