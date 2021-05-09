package ru.fasdev.tfs.screen.fragment.streamList

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.fasdev.tfs.TfsApp
import ru.fasdev.tfs.data.mapper.toStreamItem
import ru.fasdev.tfs.data.mapper.toTopicItem
import ru.fasdev.tfs.data.newPck.repository.streams.StreamsRepositoryImpl
import ru.fasdev.tfs.domain.newPck.stream.model.Stream
import ru.fasdev.tfs.mviCore.MviView
import ru.fasdev.tfs.mviCore.Store
import ru.fasdev.tfs.mviCore.entity.action.Action
import ru.fasdev.tfs.recycler.base.viewHolder.ViewType
import ru.fasdev.tfs.recycler.item.emptySearch.EmptySearchItem
import ru.fasdev.tfs.recycler.item.stream.StreamItem
import ru.fasdev.tfs.screen.fragment.streamList.mvi.StreamListAction
import ru.fasdev.tfs.screen.fragment.streamList.mvi.StreamListState
import java.util.concurrent.TimeUnit

class StreamListViewModel : ViewModel() {
    private companion object {
        const val OFFSET_ARRAY = 1
        const val NULL_INDEX = -1
        const val SEARCH_TIME_OUT = 500L
    }

    //#region Test DI
    object StreamComponent {
        val streamsRepository = StreamsRepositoryImpl(
            TfsApp.AppComponent.newUserApi,
            TfsApp.AppComponent.newStreamApi,
            TfsApp.AppComponent.newStreamDao,
            TfsApp.AppComponent.newTopicDao
        )
    }
    //#endregion

    private val store: Store<Action, StreamListState> = Store(
        initialState = StreamListState(),
        reducer = ::reducer,
        middlewares = listOf(
            ::sideActionLoadAllStreams, ::sideActionLoadSubStreams,
            ::sideActionLoadTopics, ::sideActionRemoveTopics, ::sideActionSearchStreams
        )
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
            is StreamListAction.Internal.LoadedError -> {
                return if (state.items.isEmpty()) {
                    state.copy(isLoading = false, error = action.error)
                } else {
                    state
                }
            }
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
                items = state.items
                    .toMutableList()
                    .apply { removeAll(action.topics) } // Для предотвращения дубликатов
                    .flatMap { item ->
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
                    .toMutableList()
                    .apply { removeAll(action.topics) }
            )
            else -> state
        }
    }

    private fun sideActionSearchStreams(
        actions: Observable<Action>, state: Observable<StreamListState>
    ): Observable<Action> {
        return actions
            .ofType(StreamListAction.Ui.SearchStreams::class.java)
            .debounce(SEARCH_TIME_OUT, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .observeOn(Schedulers.io())
            .switchMap { action ->
                StreamComponent.streamsRepository.searchQuery(action.query, action.isAmongSubs)
                    .toObservable()
                    .flatMap {
                        Observable.fromIterable(it)
                            .map<ViewType> { it.toStreamItem() }
                            .switchIfEmpty(Observable.just(EmptySearchItem(uId = -1)))
                            .toList()
                            .toObservable()
                    }
                    .map<StreamListAction.Internal> { StreamListAction.Internal.LoadedStreams(it) }
                    .onErrorReturn { StreamListAction.Internal.LoadedError(it) }
            }
    }

    private fun sideActionRemoveTopics(
        actions: Observable<Action>, stateFlow: Observable<StreamListState>
    ): Observable<Action> {
        return actions
            .ofType(StreamListAction.Ui.RemoveTopics::class.java)
            .withLatestFrom(stateFlow) { action, state ->
                val items = state.items

                val startIndex =
                    items.indexOfFirst { it is StreamItem && it.uId.toLong() == action.idStream }

                val subItems = items.subList(startIndex + OFFSET_ARRAY, items.size)
                var endIndex = subItems.indexOfFirst { it is StreamItem }
                if (endIndex == NULL_INDEX) endIndex = subItems.size

                val topics = subItems.subList(0, endIndex)

                return@withLatestFrom StreamListAction.Internal.RemoveTopics(
                    action.idStream,
                    topics
                )
            }
    }

    private fun sideActionLoadTopics(
        actions: Observable<Action>, state: Observable<StreamListState>
    ): Observable<Action> {
        return actions
            .ofType(StreamListAction.Ui.LoadTopics::class.java)
            .observeOn(Schedulers.io())
            .flatMap { action ->
                StreamComponent.streamsRepository.getOwnTopics(action.idStream)
                    .flatMap {
                        Observable.fromIterable(it)
                            .map { it.toTopicItem(action.idStream) }
                            .toList()
                            .toObservable()
                    }
                    .map<StreamListAction.Internal> {
                        StreamListAction.Internal.LoadedTopics(
                            action.idStream,
                            it
                        )
                    }
                    .onErrorReturn { StreamListAction.Internal.LoadedError(it) }
            }
    }

    private fun sideActionLoadSubStreams(
        actions: Observable<Action>, state: Observable<StreamListState>
    ): Observable<Action> {
        return actions
            .ofType(StreamListAction.Ui.LoadSubStreams.javaClass)
            .observeOn(Schedulers.io())
            .flatMap { _ ->
                StreamComponent.streamsRepository.getOwnSubsStreams()
                    .loadStreams()
            }
    }

    private fun sideActionLoadAllStreams(
        actions: Observable<Action>, state: Observable<StreamListState>
    ): Observable<Action> {
        return actions
            .ofType(StreamListAction.Ui.LoadAllStreams.javaClass)
            .observeOn(Schedulers.io())
            .flatMap { _ ->
                StreamComponent.streamsRepository.getAllStreams()
                    .loadStreams()
            }
    }

    private fun Observable<List<Stream>>.mapToStreamItem(): Observable<List<ViewType>> {
        return flatMap {
            Observable.fromIterable(it)
                .map { it.toStreamItem() }
                .toList()
                .toObservable()
        }
    }

    private fun Observable<List<Stream>>.loadStreams(): Observable<StreamListAction.Internal> {
        return mapToStreamItem()
            .map<StreamListAction.Internal> { StreamListAction.Internal.LoadedStreams(it) }
            .onErrorReturn { StreamListAction.Internal.LoadedError(it) }
            .startWith(StreamListAction.Internal.LoadingStreams)
    }
}