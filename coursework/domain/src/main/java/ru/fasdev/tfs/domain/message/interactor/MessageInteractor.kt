package ru.fasdev.tfs.domain.message.interactor

import ru.fasdev.tfs.domain.model.Message

interface MessageInteractor
{
    fun getMessageByChat(idChat: Int): List<Message>
    fun setSelectedReaction(idMessage: Int, emoji: String, isSelected: Boolean)
    fun sendMessage(text: String)
}