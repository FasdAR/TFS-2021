package ru.fasdev.tfs.domain.message.interactor

import ru.fasdev.tfs.domain.message.repo.MessageRepo
import ru.fasdev.tfs.domain.model.Message

class MessageInteractorImpl(private val messageRepo: MessageRepo) : MessageInteractor
{
    companion object {
        private const val CURRENT_USER = 1
    }

    override fun getMessageByChat(idChat: Int): List<Message> = messageRepo.getMessageByChat(idChat)

    override fun setSelectedReaction(idChat: Int, idMessage: Int, emoji: String, isSelected: Boolean) {
        if (isSelected) {
            messageRepo.addReaction(idChat, idMessage, CURRENT_USER, emoji)
        }
        else {
            messageRepo.removeReaction(idChat, idMessage, CURRENT_USER, emoji)
        }
    }

    override fun changeSelectedReaction(idChat: Int, idMessage: Int, emoji: String) {
        val index = getMessageByChat(idChat)
            .find { it.id == idChat }
            ?.reactions
            ?.find { it.emoji == emoji }
            ?.selectedUsersId
            ?.indexOfFirst { it == CURRENT_USER }

        val isSelected = index != -1

        setSelectedReaction(idChat, idMessage, emoji, !isSelected)
    }

    override fun sendMessage(idChat: Int, text: String) =
        messageRepo.sendMessage(idChat, CURRENT_USER, text)
}