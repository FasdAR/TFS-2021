package ru.fasdev.tfs.domain.message.repo

import ru.fasdev.tfs.domain.model.Message

interface MessageRepo
{
    fun getMessageByChat(idChat: Int): List<Message>
    fun sendMessage(idChat: Int, idUser: Int, messageText: String)
    fun addReaction(idChat: Int, idMessage: Int, idUser: Int, emoji: String)
    fun removeReaction(idChat: Int, idMessage: Int, idUser: Int, emoji: String)
}