package ru.fasdev.tfs.screen.fragment.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay2.ReplayRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.fasdev.tfs.TfsApp
import ru.fasdev.tfs.data.mapper.toMessageItem
import ru.fasdev.tfs.data.repository.messages.MessagesRepository
import ru.fasdev.tfs.data.repository.messages.MessagesRepositoryImpl
import ru.fasdev.tfs.data.repository.streams.StreamsRepository
import ru.fasdev.tfs.data.repository.streams.StreamsRepositoryImpl
import ru.fasdev.tfs.data.source.network.events.manager.EventsManager
import ru.fasdev.tfs.mviCore.MviView
import ru.fasdev.tfs.mviCore.Store
import ru.fasdev.tfs.mviCore.entity.action.Action
import ru.fasdev.tfs.recycler.item.message.MessageItem
import ru.fasdev.tfs.screen.fragment.chat.model.DirectionScroll
import ru.fasdev.tfs.screen.fragment.chat.mvi.model.PageLoadInfo
import ru.fasdev.tfs.screen.fragment.chat.mvi.ChatAction
import ru.fasdev.tfs.screen.fragment.chat.mvi.ChatState
import ru.fasdev.tfs.screen.fragment.chat.mvi.ChatUiEffect
import ru.fasdev.tfs.screen.fragment.chat.mvi.model.SendMessageInfo
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private val streamRepository: StreamsRepository,
    private val messagesRepository: MessagesRepository
) : ViewModel() {
    private companion object {
        const val LIMIT_PAGE = 20
        const val USER_ID = 402233L
    }

    private val store: Store<Action, ChatState> = Store(
        initialState = ChatState(),
        reducer = ::reducer,
        middlewares = listOf(
            ::sideActionLoadStreamInfo,
            ::sideActionLoadNextPage,
            ::sideActionSendMessage,
            ::sideActionAddReaction,
            ::sideActionRemoveReaction,
            ::sideActionListenUpdateMessage
        )
    )

    private val uiEffects: ReplayRelay<ChatUiEffect> = ReplayRelay.create()

    private val wiring = store.wire()
    private var viewBinding: Disposable = Disposables.empty()
    private var uiEffectBinding: Disposable = Disposables.empty()

    override fun onCleared() {
        super.onCleared()
        wiring.dispose()
    }

    fun bind(view: MviView<Action, ChatState>) {
        viewBinding = store.bind(view)
    }

    fun bind(render: (ChatUiEffect) -> Unit) {
        uiEffectBinding = uiEffects
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                render(it)
            }
    }

    fun unBind() {
        viewBinding.dispose()
        uiEffectBinding.dispose()
    }

    private fun reducer(state: ChatState, action: Action): ChatState {
        Log.d("NEW_ACTION_REDUCER", action.toString())
        return when (action) {
            is ChatAction.Internal.LoadedStreamInfo -> state.copy(
                streamName = action.streamName,
                topicName = action.topicName
            )
            is ChatAction.Internal.LoadedError -> state.copy(
                isLoading = false,
                error = action.error
            )
            is ChatAction.Internal.LoadingPage -> state.copy(isLoading = true, error = null)
            is ChatAction.Internal.UpdateMessages -> {
                state.copy(items = action.items)
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
            is ChatAction.Internal.SendedError -> {
                uiEffects.accept(ChatUiEffect.ErrorSnackbar(action.error.message.toString()))
                state
            }
            is ChatAction.Ui.OpenEmojiDialog -> {
                uiEffects.accept(ChatUiEffect.OpenEmojiDialog)
                state.copy(idSelectedMessage = action.idMessage)
            }
            else -> state
        }
    }

    private fun sideActionListenUpdateMessage(
        actions: Observable<Action>,
        stateFlow: Observable<ChatState>
    ): Observable<Action> {
        return actions
            .ofType(ChatAction.Internal.StartListenMessage::class.java)
            .observeOn(Schedulers.io())
            .flatMap {
                return@flatMap messagesRepository.listenUpdate(it.streamName, it.topicName)
                    .flatMap {
                        Observable.fromIterable(it)
                            .map {
                                val isOwnMessage = it.sender.id == USER_ID
                                it.toMessageItem(isOwnMessage)
                            }
                            .toList()
                            .toObservable()
                    }
                    .withLatestFrom(stateFlow) { updateItems, state ->
                        val items = state.items.toMutableList()
                        updateItems.forEach { item ->
                            val indexMessage =
                                items.indexOfFirst { it is MessageItem && it.uId == item.uId }

                            if (indexMessage != -1) {
                                items[indexMessage] = item
                            } else {
                                items.add(0, item)
                            }
                        }

                        return@withLatestFrom items
                    }
                    .map<ChatAction.Internal> { ChatAction.Internal.UpdateMessages(it) }
                    .onErrorReturn { ChatAction.Internal.SendedError(it) }
            }
    }

    private fun sideActionLoadStreamInfo(
        actions: Observable<Action>,
        state: Observable<ChatState>
    ): Observable<Action> {
        return actions
            .ofType(ChatAction.Ui.LoadStreamInfo::class.java)
            .observeOn(Schedulers.io())
            .flatMap { action ->
                val streamSource = streamRepository.getStreamById(action.idStream)
                val topicSource = streamRepository.getTopicById(action.idTopic)

                streamSource
                    .zipWith(topicSource) { stream, topic -> stream.name to topic.name }
                    .toObservable()
                    .flatMap {
                        Observable.just(
                            ChatAction.Internal.LoadedStreamInfo(it.first, it.second),
                            ChatAction.Internal.StartListenMessage(it.first, it.second),
                            ChatAction.Ui.LoadPageMessages(null, DirectionScroll.UP)
                        )
                    }
                    .onErrorReturn { ChatAction.Internal.LoadedError(it) }
            }
    }

    private fun sideActionLoadNextPage(
        actions: Observable<Action>,
        stateFlow: Observable<ChatState>
    ): Observable<Action> {
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
                messagesRepository
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
                    .map<ChatAction.Internal> {
                        ChatAction.Internal.LoadedPage(
                            it,
                            loadInfo.directionScroll
                        )
                    }
                    .onErrorReturn { ChatAction.Internal.LoadedError(it) }
                    .startWith(ChatAction.Internal.LoadingPage)
            }
    }

    private fun sideActionSendMessage(
        actions: Observable<Action>,
        stateFlow: Observable<ChatState>
    ): Observable<Action> {
        return actions
            .ofType(ChatAction.Ui.SendMessage::class.java)
            .observeOn(Schedulers.io())
            .withLatestFrom(stateFlow) { action, state ->
                SendMessageInfo(state.streamName.toString(), state.topicName.toString(), action)
            }
            .flatMap { action ->
                messagesRepository.sendMessage(
                    action.streamName,
                    action.topicName,
                    action.action.textMessage
                )
                    .toObservable<ChatAction.Internal>()
                    .map<ChatAction.Internal> { ChatAction.Internal.SendedMessage }
                    .onErrorReturn { ChatAction.Internal.SendedError(it) }
            }
    }

    private fun sideActionAddReaction(
        actions: Observable<Action>,
        stateFlow: Observable<ChatState>
    ): Observable<Action> {
        return actions
            .ofType(ChatAction.Ui.SelectedReaction::class.java)
            .observeOn(Schedulers.io())
            .withLatestFrom(stateFlow) { action, state ->
                if (action.idMessage == null) {
                    return@withLatestFrom action.copy(idMessage = state.idSelectedMessage)
                } else {
                    action
                }
            }
            .flatMap { action ->
                messagesRepository.addReaction(action.idMessage ?: -1, action.emoji)
                    .toObservable<ChatAction.Internal>()
                    .map<ChatAction.Internal> { ChatAction.Internal.SelectedReaction }
                    .onErrorReturn { ChatAction.Internal.SendedError(it) }
            }
    }

    private fun sideActionRemoveReaction(
        actions: Observable<Action>,
        stateFlow: Observable<ChatState>
    ): Observable<Action> {
        return actions
            .ofType(ChatAction.Ui.UnSelectedReaction::class.java)
            .observeOn(Schedulers.io())
            .withLatestFrom(stateFlow) { action, state ->
                if (action.idMessage == null) {
                    return@withLatestFrom action.copy(idMessage = state.idSelectedMessage)
                } else {
                    action
                }
            }
            .flatMap { action ->
                messagesRepository.removeReaction(action.idMessage ?: -1, action.emoji)
                    .toObservable<ChatAction.Internal>()
                    .map<ChatAction.Internal> { ChatAction.Internal.UnSelectedReaction }
                    .onErrorReturn { ChatAction.Internal.SendedError(it) }
            }
    }
}