package ru.fasdev.tfs.screen.fragment.streamList

import android.util.Log
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.fasdev.tfs.TfsApp
import ru.fasdev.tfs.data.newPck.repository.streams.StreamsRepositoryImpl
import ru.fasdev.tfs.mviCore.MviView
import ru.fasdev.tfs.mviCore.Store
import ru.fasdev.tfs.mviCore.entity.action.Action
import ru.fasdev.tfs.recycler.item.stream.StreamItem
import ru.fasdev.tfs.recycler.item.topic.TopicItem
import ru.fasdev.tfs.screen.fragment.streamList.mvi.StreamListAction
import ru.fasdev.tfs.screen.fragment.streamList.mvi.StreamListState

class StreamListViewModel : ViewModel() {
    private companion object {
        const val OFFSET_ARRAY = 1
        const val NULL_INDEX = -1
    }
    //#region Test DI
    object StreamComponent {
        val streamsRepository = StreamsRepositoryImpl(TfsApp.AppComponent.newUserApi, TfsApp.AppComponent.newStreamApi)
    }
    //#endregion

    private val store: Store<Action, StreamListState> = Store(
        initialState = StreamListState(),
        reducer = ::reducer,
        middlewares = listOf(::sideActionLoadAllStreams, ::sideActionLoadSubStreams,
            ::sideActionLoadTopics, ::sideActionRemoveTopics)
    )

    private val wiring = store.wire()
    private var viewBinding: Disposable = Disposables.empty()

    override fun onCleared() {
        super.onCleared()
        wiring.dispose()
    }

    fun bind(view: MviView<Action, StreamListState>) {
        viewBinding = store.bind(view)
    }

    fun unBind() {
        viewBinding.dispose()
    }

    private fun reducer(state: StreamListState, action: Action): StreamListState {
        return when (action) {
            is StreamListAction.Internal.LoadedError -> state.copy(
                isLoading = false,
                error = action.error
            )
            is StreamListAction.Internal.LoadingStreams -> state.copy(
                isLoading = true,
                error = null
            )
            is StreamListAction.Internal.LoadedStreams -> state.copy(
                isLoading = false,
                error = null,
                items = action.streams
            )
            is StreamListAction.Internal.LoadedTopics -> state.copy(
                error = null,
                items = state.items.flatMap { item ->
                    if (item is StreamItem && item.uId.toLong() == action.idStream) {
                        return@flatMap listOf(item.copy(isOpen = true)) + action.topics
                    }

                    return@flatMap listOf(item)
                }
            )
            is StreamListAction.Internal.RemoveTopics -> state.copy(
                error = null,
                items = state.items
                    .map { item ->
                        if (item is StreamItem && item.uId.toLong() == action.idStream) {
                            return@map item.copy(isOpen = false)
                        } else {
                            return@map item
                        }
                    }
                    .toMutableList().apply { removeAll(action.topics) }
            )
            else -> state
        }
    }

    private fun sideActionRemoveTopics(
        acions: Observable<Action>, state: Observable<StreamListState>
    ) : Observable<Action> {
        return acions
            .ofType(StreamListAction.Ui.RemoveTopics::class.java)
            .withLatestFrom(state) { action, state ->
                val items = state.items

                val startIndex = items.indexOfFirst { it is StreamItem && it.uId.toLong() == action.idStream }

                val subItems = items.subList(startIndex + OFFSET_ARRAY, items.size)
                var endIndex = subItems.indexOfFirst { it is StreamItem }
                if (endIndex == NULL_INDEX) endIndex = subItems.size
                val topics = subItems.subList(0, endIndex)

                return@withLatestFrom StreamListAction.Internal.RemoveTopics(action.idStream, topics)
            }
    }

    private fun sideActionLoadTopics(
        acions: Observable<Action>, state: Observable<StreamListState>
    ) : Observable<Action> {
        return acions
            .ofType(StreamListAction.Ui.LoadTopics::class.java)
            .observeOn(Schedulers.io())
            .flatMap { action ->
                StreamComponent.streamsRepository.getOwnTopics(action.idStream)
                    .flatMap {
                        Observable.fromIterable(it)
                            .map {
                                TopicItem(
                                    uId = it.id.toInt(),
                                    idStream = action.idStream,
                                    nameTopic = it.name,
                                    messageCount = 0
                                )
                            }
                            .toList()
                            .toObservable()
                    }
                    .map<StreamListAction.Internal> { StreamListAction.Internal.LoadedTopics(action.idStream, it) }
                    .onErrorReturn { StreamListAction.Internal.LoadedError(it) }
            }
    }

    private fun sideActionLoadSubStreams(
        acions: Observable<Action>, state: Observable<StreamListState>
    ) : Observable<Action> {
        return acions
            .ofType(StreamListAction.Ui.LoadSubStreams.javaClass)
            .observeOn(Schedulers.io())
            .flatMap { _ ->
                StreamComponent.streamsRepository.getOwnSubsStreams()
                    .flatMap {
                        Observable.fromIterable(it)
                            .map {
                                StreamItem(
                                    uId = it.id.toInt(),
                                    nameTopic = it.name
                                )
                            }
                            .toList()
                            .toObservable()
                    }
                    .map<StreamListAction.Internal> { StreamListAction.Internal.LoadedStreams(it) }
                    .onErrorReturn { StreamListAction.Internal.LoadedError(it) }
                    .startWith(StreamListAction.Internal.LoadingStreams)
            }
    }

    private fun sideActionLoadAllStreams(
        acions: Observable<Action>, state: Observable<StreamListState>
    ) : Observable<Action> {
        return acions
            .ofType(StreamListAction.Ui.LoadAllStreams.javaClass)
            .observeOn(Schedulers.io())
            .flatMap { _ ->
                StreamComponent.streamsRepository.getAllStreams()
                    .flatMap {
                        Observable.fromIterable(it)
                            .map {
                                StreamItem(
                                    uId = it.id.toInt(),
                                    nameTopic = it.name
                                )
                            }
                            .toList()
                            .toObservable()
                    }
                    .map<StreamListAction.Internal> { StreamListAction.Internal.LoadedStreams(it) }
                    .onErrorReturn { StreamListAction.Internal.LoadedError(it) }
                    .startWith(StreamListAction.Internal.LoadingStreams)
            }
    }
}