package ru.fasdev.tfs.screen.fragment.chat

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
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
import ru.fasdev.tfs.screen.fragment.chat.mvi.model.PageLoadInfo
import ru.fasdev.tfs.screen.fragment.chat.mvi.ChatAction
import ru.fasdev.tfs.screen.fragment.chat.mvi.ChatState
import ru.fasdev.tfs.screen.fragment.chat.mvi.model.SendMessageInfo
import java.util.concurrent.TimeUnit

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
            ::sideActionLoadTopicName, ::sideActionLoadNextPage, ::sideActionSendMessage)
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
            is ChatAction.Internal.LoadedError -> state.copy(isLoading = false, error = action.error)
            is ChatAction.Internal.LoadingPage -> state.copy(isLoading = true, error = null)
            is ChatAction.Internal.UpdateMessage -> {
                val indexMessage = state.items.indexOfFirst { it is MessageItem && it.uId == action.item.uId }
                if (indexMessage != -1) {
                    val items = state.items.toMutableList()
                    items[indexMessage] = action.item
                    state.copy(items = items)
                }
                else {
                    state.copy(items = state.items + listOf(action.item))
                }
            }
            is ChatAction.Internal.LoadedPage -> {
                val isUpScroll = action.direction == DirectionScroll.UP

                val items = if (isUpScroll) {
                    state.items + action.items
                } else {
                    action.items + state.items
                }

                state.copy(items = items.distinct())
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
                    .map<ChatAction.Internal> { ChatAction.Internal.LoadedStreamName(it.name) }
                    .onErrorReturn { ChatAction.Internal.LoadedError(it) }
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
                    .map<ChatAction.Internal> { ChatAction.Internal.LoadedTopicName(it.name) }
                    .onErrorReturn { ChatAction.Internal.LoadedError(it) }
            }
    }

    private fun sideActionLoadNextPage(
        actions: Observable<Action>,
        stateFlow: Observable<ChatState>
    ) : Observable<Action> {
        return actions
            .ofType(ChatAction.Ui.LoadPageMessages::class.java)
            .distinctUntilChanged()
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(Schedulers.io())
            .filter { it.direction == DirectionScroll.UP }
            .withLatestFrom(stateFlow) { action, state ->
                val isUpScroll = action.direction == DirectionScroll.UP

                val afterMessageCount = if (isUpScroll) 0 else LIMIT_PAGE
                val beforeMessageCount = if (!isUpScroll) 0 else LIMIT_PAGE

                PageLoadInfo(
                    state.streamName.toString(),
                    state.topicName.toString(),
                    action.anchorMessageId,
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
                            .sorted { item1, item2 -> item2.date.compareTo(item1.date) }
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

    private fun sideActionSendMessage(
        actions: Observable<Action>,
        stateFlow: Observable<ChatState>
    ) : Observable<Action> {
        return actions
            .ofType(ChatAction.Ui.SendMessage::class.java)
            .observeOn(Schedulers.io())
            .withLatestFrom(stateFlow) { action, state ->
                SendMessageInfo(state.streamName.toString(), state.topicName.toString(), action)
            }
            .flatMap { action ->
                ChatComponent.messagesRepository.sendMessage(
                    action.streamName,
                    action.topicName,
                    action.action.textMessage
                )
                .toObservable<ChatAction.Internal>()
                .map<ChatAction.Internal> { ChatAction.Internal.SendedMessage }
                .onErrorReturn { ChatAction.Internal.SendedError(it) }
            }
    }
}