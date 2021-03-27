package ru.fasdev.tfs.domain.message.interactor

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.fasdev.tfs.domain.TestError
import ru.fasdev.tfs.domain.message.repo.MessageRepo
import ru.fasdev.tfs.domain.model.Message

class MessageInteractorImpl(private val messageRepo: MessageRepo) : MessageInteractor {
    companion object {
        private const val CURRENT_USER = 1
    }

    override fun getMessageByChat(idChat: Int): Single<List<Message>> {
        return Single.just(messageRepo.getMessageByChat(idChat))
            .doOnSuccess { TestError.testError() }
            .subscribeOn(Schedulers.io())
    }

    override fun setSelectedReaction(
        idChat: Int, idMessage: Int,
        emoji: String, isSelected: Boolean
    ): Completable {
        return Completable.fromCallable {
            TestError.testError()

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
            .flatMapObservable { Observable.fromIterable(it.reactions) }
            .filter { it.emoji == emoji }
            .firstElement()
            .map { it.isSelected }
            .flatMapCompletable { isSelected ->
                Completable.fromCallable {
                    setSelectedReaction(idChat, idMessage, emoji, !isSelected)
                }
            }
            .subscribeOn(Schedulers.io())
    }

    override fun sendMessage(idChat: Int, text: String): Completable {
        return Completable.fromCallable {
            TestError.testError()

            messageRepo.sendMessage(idChat, CURRENT_USER, text)
        }.subscribeOn(Schedulers.io())
    }
}
