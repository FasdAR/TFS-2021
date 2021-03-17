package ru.fasdev.tfs.domain.message.interactor

import ru.fasdev.tfs.domain.model.Message

interface MessageInteractor {
    fun getMessageByChat(idChat: Int): List<Message>
    fun setSelectedReaction(idChat: Int, idMessage: Int, emoji: String, isSelected: Boolean)
    fun changeSelectedReaction(idChat: Int, idMessage: Int, emoji: String)
    fun sendMessage(idChat: Int, text: String)
}
