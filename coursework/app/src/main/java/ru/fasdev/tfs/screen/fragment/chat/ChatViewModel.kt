package ru.fasdev.tfs.screen.fragment.chat

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import ru.fasdev.tfs.mviCore.MviView
import ru.fasdev.tfs.mviCore.Store
import ru.fasdev.tfs.mviCore.entity.action.Action
import ru.fasdev.tfs.screen.fragment.chat.mvi.ChatState

class ChatViewModel : ViewModel() {

    private val store: Store<Action, ChatState> = Store(
        initialState = ChatState(),
        reducer = ::reducer,
        middlewares = listOf()
    )

    private val wiring = store.wire()
    private var viewBinding: Disposable = Disposables.empty()

    override fun onCleared() {
        super.onCleared()
        wiring.dispose()
    }

    fun bind(view: MviView<Action, ChatState>) {
        viewBinding = store.bind(view)
    }

    fun unBind() {
        viewBinding.dispose()
    }

    private fun reducer(state: ChatState, action: Action): ChatState {
        return when (action) {
            else -> state
        }
    }

    /*
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
    }*/
}