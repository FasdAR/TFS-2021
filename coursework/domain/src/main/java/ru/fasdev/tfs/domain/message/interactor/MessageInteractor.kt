package ru.fasdev.tfs.domain.message.interactor

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.fasdev.tfs.domain.model.Message

interface MessageInteractor {
    fun getMessageByChat(idChat: Int): Single<List<Message>>
    fun setSelectedReaction(idChat: Int, idMessage: Int, emoji: String, isSelected: Boolean): Completable
    fun changeSelectedReaction(idChat: Int, idMessage: Int, emoji: String): Completable
    fun sendMessage(idChat: Int, text: String): Completable
}
