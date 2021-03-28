package ru.fasdev.tfs.domain.message.interactor

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.fasdev.tfs.domain.TestError
import ru.fasdev.tfs.domain.message.repo.MessageRepo
import ru.fasdev.tfs.domain.model.Message
import sun.rmi.runtime.Log

class MessageInteractorImpl(private val messageRepo: MessageRepo) : MessageInteractor {
    companion object {
        private const val CURRENT_USER = 1
    }

    override fun getMessageByChat(idChat: Int): Single<List<Message>> {
        return Single.just(messageRepo.getMessageByChat(idChat))
            .doOnSuccess { TestError.testError("Get messages") }
            .subscribeOn(Schedulers.io())
    }

    override fun setSelectedReaction(
        idChat: Int, idMessage: Int,
        emoji: String, isSelected: Boolean
    ): Completable {
        return Completable.fromCallable {
            TestError.testError("Selected Reaction")

            if (isSelected) {
                messageRepo.addReaction(idChat, idMessage, CURRENT_USER, emoji)
            } else {
                messageRepo.removeReaction(idChat, idMessage, CURRENT_USER, emoji)
            }
        }.subscribeOn(Schedulers.io())
    }

    override fun changeSelectedReaction(idChat: Int, idMessage: Int, emoji: String): Completable {
        return getMessageByChat(idChat)
            .flatMapObservable { array -> Observable.fromIterable(array) }
            .filter { it.id == idMessage }
            .firstElement()
            .flatMapSingle {
                val indexReaction = it.reactions.indexOfFirst { it.emoji == emoji }
                if (indexReaction != -1) {
                    Single.just(it.reactions[indexReaction].isSelected)
                }
                else {
                    Single.just(false)
                }
            }
            .flatMapCompletable { isSelected ->
                setSelectedReaction(idChat, idMessage, emoji, !isSelected)
            }
            .subscribeOn(Schedulers.io())
    }

    override fun sendMessage(idChat: Int, text: String): Completable {
        return Completable.fromCallable {
            TestError.testError("Send Message")

            messageRepo.sendMessage(idChat, CURRENT_USER, text)
        }.subscribeOn(Schedulers.io())
    }
}
