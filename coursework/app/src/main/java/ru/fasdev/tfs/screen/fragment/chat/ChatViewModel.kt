package ru.fasdev.tfs.screen.fragment.chat

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.fasdev.tfs.TfsApp
import ru.fasdev.tfs.data.mapper.toMessageItem
import ru.fasdev.tfs.data.newPck.repository.messages.MessagesRepositoryImpl
import ru.fasdev.tfs.data.newPck.repository.streams.StreamsRepositoryImpl
import ru.fasdev.tfs.mviCore.MviView
import ru.fasdev.tfs.mviCore.Store
import ru.fasdev.tfs.mviCore.entity.action.Action
import ru.fasdev.tfs.recycler.item.message.MessageItem
import ru.fasdev.tfs.screen.fragment.chat.model.DirectionScroll
import ru.fasdev.tfs.screen.fragment.chat.model.PageLoadInfo
import ru.fasdev.tfs.screen.fragment.chat.mvi.ChatAction
import ru.fasdev.tfs.screen.fragment.chat.mvi.ChatState
import ru.fasdev.tfs.screen.fragment.streamList.mvi.StreamListAction

class ChatViewModel : ViewModel() {
    private companion object {
        const val LIMIT_PAGE = 20
        const val USER_ID = 402233L
    }

    //#region Test DI
    object ChatComponent {
        val streamsRepository = StreamsRepositoryImpl(
            TfsApp.AppComponent.newUserApi,
            TfsApp.AppComponent.newStreamApi,
            TfsApp.AppComponent.newStreamDao,
            TfsApp.AppComponent.newTopicDao
        )

        val messagesRepository = MessagesRepositoryImpl(
            TfsApp.AppComponent.json,
            TfsApp.AppComponent.newMessagesApi
        )
    }
    //#endregion

    private val store: Store<Action, ChatState> = Store(
        initialState = ChatState(),
        reducer = ::reducer,
        middlewares = listOf(::sideActionLoadStreamName,
            ::sideActionLoadTopicName, ::sideActionLoadNextPage)
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
            is ChatAction.Internal.LoadedStreamName -> state.copy(streamName = action.streamName)
            is ChatAction.Internal.LoadedTopicName -> state.copy(topicName = action.topicName)
            is ChatAction.Internal.LoadedPage -> {
                val isUpScroll = action.direction == DirectionScroll.UP

                val items = if (isUpScroll) {
                    state.items + action.items
                } else {
                    action.items + state.items
                }

                state.copy(items = items)
            }
            else -> state
        }
    }

    private fun sideActionLoadStreamName(
        actions: Observable<Action>,
        state: Observable<ChatState>
    ) : Observable<Action> {
        return actions
            .ofType(ChatAction.Ui.LoadStreamInfo::class.java)
            .observeOn(Schedulers.io())
            .flatMap { action ->
                ChatComponent.streamsRepository.getStreamById(action.idStream)
                    .toObservable()
                    .map { ChatAction.Internal.LoadedStreamName(it.name) }
            }
    }

    private fun sideActionLoadTopicName(
        actions: Observable<Action>,
        state: Observable<ChatState>
    ) : Observable<Action> {
        return actions
            .ofType(ChatAction.Ui.LoadTopicInfo::class.java)
            .observeOn(Schedulers.io())
            .flatMap { action ->
                ChatComponent.streamsRepository.getTopicById(action.idTopic)
                    .toObservable()
                    .map { ChatAction.Internal.LoadedTopicName(it.name) }
            }
    }

    private fun sideActionLoadNextPage(
        actions: Observable<Action>,
        stateFlow: Observable<ChatState>
    ) : Observable<Action> {
        return actions
            .ofType(ChatAction.Ui.LoadPageMessages::class.java)
            .observeOn(Schedulers.io())
            .withLatestFrom(stateFlow) { action, state ->
                val isUpScroll = action.direction == DirectionScroll.UP

                val idAnchorMessage: Long? = if (isUpScroll) {
                    state.items.filterIsInstance<MessageItem>().firstOrNull()?.uId?.toLong()
                } else {
                    state.items.filterIsInstance<MessageItem>().lastOrNull()?.uId?.toLong()
                }

                val afterMessageCount = if (isUpScroll) 0 else LIMIT_PAGE
                val beforeMessageCount = if (!isUpScroll) 0 else LIMIT_PAGE

                PageLoadInfo(
                    state.streamName.toString(),
                    state.topicName.toString(),
                    idAnchorMessage,
                    afterMessageCount,
                    beforeMessageCount,
                    action.direction
                )
            }
            .flatMap { loadInfo ->
                ChatComponent.messagesRepository
                    .getMessagesPage(
                        nameStream = loadInfo.nameStream,
                        nameTopic = loadInfo.nameTopic,
                        idAnchorMessage = loadInfo.idAnchorMessage,
                        afterMessageCount = loadInfo.afterMessageCount,
                        beforeMessageCount = loadInfo.beforeMessageCount
                    )
                    .flatMap {
                        Observable.fromIterable(it)
                            .sorted { item1, item2 -> item1.date.compareTo(item2.date) }
                            .map {
                                val isOwnMessage = it.sender.id == USER_ID
                                it.toMessageItem(isOwnMessage)
                            }
                            .toList()
                            .toObservable()
                    }
                    .map<ChatAction.Internal> { ChatAction.Internal.LoadedPage(it, loadInfo.directionScroll) }
                    .onErrorReturn { ChatAction.Internal.LoadedError(it) }
                    .startWith(ChatAction.Internal.LoadingPage)
            }
    }

    /*
    .map<OwnProfileAction.Internal> { OwnProfileAction.Internal.LoadedUser(it) }
                    .onErrorReturn { OwnProfileAction.Internal.LoadedError(it) }
                    .startWith(OwnProfileAction.Internal.LoadingUser)
     */

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