package ru.fasdev.tfs.screen.fragment.chat

import androidx.lifecycle.ViewModel
import com.freeletics.rxredux.StateAccessor
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import ru.fasdev.tfs.TfsApp
import ru.fasdev.tfs.data.old.mapper.mapToUiList
import ru.fasdev.tfs.data.old.repo.MessageRepoImpl
import ru.fasdev.tfs.di.module.ChatDomainModule
import ru.fasdev.tfs.domain.old.message.interactor.MessageInteractor
import ru.fasdev.tfs.domain.old.message.interactor.MessageInteractorImpl
import ru.fasdev.tfs.domain.old.message.model.DirectionScroll
import ru.fasdev.tfs.screen.fragment.chat.mvi.ChatAction
import ru.fasdev.tfs.screen.fragment.chat.mvi.ChatState
import ru.fasdev.tfs.view.MviView

class ChatViewModel : ViewModel() {
    object ChatComponent {
        val messageRepo =
            ChatDomainModule.getMessageRepo(
                TfsApp.AppComponent.chatApi,
                TfsApp.AppComponent.json,
                TfsApp.AppComponent.roomDb,
                TfsApp.AppComponent.messageDao,
                TfsApp.AppComponent.userDao,
                TfsApp.AppComponent.reactionDao
            )
        val messageInteractor = MessageInteractorImpl(messageRepo)
    }

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val interactor: MessageInteractor = ChatComponent.messageInteractor

    private val inputRelay: Relay<ChatAction> = PublishRelay.create()
    val input: Consumer<ChatAction> get() = inputRelay

    val store = inputRelay.reduxStore(
        initialState = ChatState(),
        sideEffects = listOf(::sendMessageSideEffect, ::selectedReactionSideEffect, ::loadPagingSideEffect),
        reducer = ::reducer
    )

    fun attachView(mviView: MviView<ChatState>) {
        compositeDisposable += store.subscribe{mviView.render(it)}
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun reducer(state: ChatState, action: ChatAction): ChatState {
        return when (action) {
            is ChatAction.LoadData -> state.copy(isLoading = true, error = null)
            is ChatAction.LoadedData -> state.copy(isLoading = false, error = null, listItems = action.array)
            is ChatAction.ErrorLoading -> state.copy(isLoading = false, error = action.error)
            else -> state
        }
    }

    private fun sendMessageSideEffect(
        action: Observable<ChatAction>,
        state: StateAccessor<ChatState>
    ): Observable<ChatAction> {
        return action
            .ofType(ChatAction.SideEffectSendMessage::class.java)
            .filter{ it.textMessage.isNotEmpty() }
            .switchMap {
                interactor
                    .sendMessage(it.streamName, it.topicName, it.textMessage)
                    .subscribeOn(Schedulers.io())
                    .toObservable<ChatAction>()
                    .onErrorReturn { error ->
                        ChatAction.ErrorLoading(error)
                    }
            }
    }

    private fun selectedReactionSideEffect(
        action: Observable<ChatAction>,
        state: StateAccessor<ChatState>
    ) : Observable<ChatAction> {
        return action
            .ofType(ChatAction.SideEffectSelectedReaction::class.java)
            .switchMap {
                interactor
                    .setSelectionReaction(it.idMessage, it.emoji, it.isSelected)
                    .subscribeOn(Schedulers.io())
                    .toObservable<ChatAction>()
                    .onErrorReturn { error ->
                        ChatAction.ErrorLoading(error)
                    }
            }
    }

    private fun loadPagingSideEffect(
        action: Observable<ChatAction>,
        state: StateAccessor<ChatState>
    ) : Observable<ChatAction> {
        return action
            .ofType(ChatAction.SideEffectLoadingPage::class.java)
            .switchMap { action ->
                interactor
                    .getMessagesByTopic(
                        nameStream = action.streamName,
                        nameTopic = action.topicName,
                        anchorMessage = action.lastVisibleId,
                        direction = action.direction
                    )
                    .subscribeOn(Schedulers.io())
                    .map { newList ->
                        newList.mapToUiList(MessageRepoImpl.USER_ID).reversed()
                    }
                    .map {
                        val oldState = state()

                        if (action.direction == DirectionScroll.UP) {
                            oldState.listItems + it
                        } else {
                            it + oldState.listItems
                        }
                    }
                    .map { it.distinct() }
                    .toObservable()
                    .map {
                        ChatAction.LoadedData(it)
                    }
                    .map { it as ChatAction }
                    .onErrorReturn { error ->
                        ChatAction.ErrorLoading(error)
                    }
            }
    }
}