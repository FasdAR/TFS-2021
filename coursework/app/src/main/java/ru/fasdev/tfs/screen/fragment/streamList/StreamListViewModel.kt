package ru.fasdev.tfs.screen.fragment.streamList

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import ru.fasdev.tfs.mviCore.MviView
import ru.fasdev.tfs.mviCore.Store
import ru.fasdev.tfs.mviCore.entity.action.Action
import ru.fasdev.tfs.recycler.item.stream.StreamItem
import ru.fasdev.tfs.screen.fragment.streamList.mvi.StreamListAction
import ru.fasdev.tfs.screen.fragment.streamList.mvi.StreamListState

class StreamListViewModel : ViewModel() {
    private val store: Store<Action, StreamListState> = Store(
        initialState = StreamListState(),
        reducer = ::reducer,
        middlewares = listOf()
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
                items = state.items.flatMap {
                    if (it.uId.toLong() == action.idStream) {
                        return@flatMap listOf(it) + action.topics
                    }
                    return@flatMap listOf(it)
                }
            )
            is StreamListAction.Internal.RemoveTopics -> state.copy(
                error = null,
                items = state.items.toMutableList().apply {
                    val startIndex = indexOfFirst {
                        it is StreamItem && it.uId.toLong() == action.idStream
                    }
                    val endIndex = subList(startIndex, size).indexOfFirst {
                        it is StreamItem
                    }

                    val topics = subList(startIndex, endIndex)
                    removeAll(topics)
                }
            )
            else -> state
        }
    }

    /*
     private fun sideActionLoadOwnUser(
        actions: Observable<Action>,
        state: Observable<OwnProfileState>
    ): Observable<Action> {
        return actions
            .ofType(OwnProfileAction.Ui.LoadUser.javaClass)
            .observeOn(Schedulers.io())
            .flatMap { _ ->
                ProfileComponent.usersRepository.getOwnUser()
                    .toObservable()
                    .map<OwnProfileAction.Internal> { OwnProfileAction.Internal.LoadedUser(it) }
                    .onErrorReturn { OwnProfileAction.Internal.LoadedError(it) }
                    .startWith(OwnProfileAction.Internal.LoadingUser)
            }
    }
     */

    /*
    object StreamComponent {
        val streamRepo = StreamDomainModule.getStreamRepo(
            TfsApp.AppComponent.streamApi,
            TfsApp.AppComponent.streamDao,
            TfsApp.AppComponent.topicDao
        )
        val streamInteractor = StreamDomainModule.getStreamInteractor(streamRepo)
    }

    private val streamInteractor: StreamInteractor = StreamComponent.streamInteractor

    private val compositeDisposable = CompositeDisposable()
    private val inputRelay: Relay<StreamListAction> = PublishRelay.create()
    val input: Consumer<StreamListAction> get() = inputRelay

    val store = inputRelay.reduxStore(
        initialState = StreamListState(),
        sideEffects = listOf(::loadAllStreamsSideEffect, ::searchSideEffect, ::loadTopicsSideEffect),
        reducer = ::reducer
    )

    fun attachView(mviView: MviView<StreamListState>) {
        compositeDisposable += store.subscribe { mviView.render(it) }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun reducer(state: StreamListState, action: StreamListAction): StreamListState {
        return when (action) {
            is StreamListAction.ErrorLoading -> state.copy(isLoading = false, error = action.error)
            is StreamListAction.LoadData -> state.copy(isLoading = true, error = null)
            is StreamListAction.LoadedArray -> state.copy(isLoading = false, error = null, itemsList = action.streams)
            else -> state
        }
    }

    private fun Flowable<List<Stream>>.mapToDomain(): Flowable<List<StreamItem>> {
        return concatMap {
            Flowable.fromIterable(it)
                .map { it.toStreamUi() }
                .toList()
                .flatMapPublisher { Flowable.just(it) }
        }
    }

    private fun getStreamSource(mode: Int): Flowable<List<Stream>> {
        return if (mode == StreamListFragment.ALL_MODE) streamInteractor.getAllStreams()
        else streamInteractor.getSubStreams()
    }

    private fun loadAllStreamsSideEffect(
        action: Observable<StreamListAction>,
        state: StateAccessor<StreamListState>
    ): Observable<StreamListAction> {
        return action
            .ofType(StreamListAction.SideEffectLoadAllStreams::class.java)
            .switchMap {
                getStreamSource(it.mode)
                    .subscribeOn(Schedulers.io())
                    .mapToDomain()
                    .map {
                        StreamListAction.LoadedArray(it)
                    }
                    .map { it as StreamListAction }
                    .toObservable()
                    .onErrorReturn { error -> StreamListAction.ErrorLoading(error) }
                    .startWith(StreamListAction.LoadData)
            }
    }

    private fun searchSideEffect(
        action: Observable<StreamListAction>,
        state: StateAccessor<StreamListState>
    ): Observable<StreamListAction> {
        return action
            .ofType(StreamListAction.SideEffectSearchStreams::class.java)
            .debounce(500, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .switchMap {
                val isSub = it.mode == StreamListFragment.SUBSCRIBED_MODE
                streamInteractor
                    .searchStream(it.query.trim().toLowerCase(), isSub)
                    .subscribeOn(Schedulers.io())
                    .flatMapObservable(::fromIterable)
                    .map { it.toStreamUi() }
                    .toList()
                    .map {
                        StreamListAction.LoadedArray(it)
                    }
                    .map { it as StreamListAction }
                    .toObservable()
                    .onErrorReturn { error -> StreamListAction.ErrorLoading(error) }
                    .startWith(StreamListAction.LoadData)
            }
    }

    private fun loadTopicsSideEffect(
        action: Observable<StreamListAction>,
        state: StateAccessor<StreamListState>
    ): Observable<StreamListAction> {
        return action
            .ofType(StreamListAction.SideEffectLoadTopics::class.java)
            .switchMap { action ->
                val state = state()
                val selectedStream =
                    state.itemsList.filter { it is StreamItem }.map { it as StreamItem }
                        .findLast { it.uId == action.idStream }
                streamInteractor.getAllTopics(action.idStream.toLong())
                    .subscribeOn(Schedulers.io())
                    .concatMap {
                        Flowable.fromIterable(it)
                            .map {
                                it.toTopicUi(selectedStream?.nameTopic.toString())
                            }
                            .toList()
                            .map { topics ->
                                val currentArray = state.itemsList.toMutableList()
                                Log.d("CURRENT_STATE", state.toString())

                                val currentStreamIndex =
                                    currentArray.indexOfFirst { it.uId == action.idStream && it is StreamItem }
                                val stream = currentArray[currentStreamIndex] as StreamItem
                                currentArray[currentStreamIndex] =
                                    stream.copy(isOpen = action.opened)

                                currentArray.removeAll(topics)

                                if (action.opened) {
                                    currentArray.addAll(currentStreamIndex + 1, topics)
                                }

                                return@map currentArray
                            }
                            .flatMapPublisher { Flowable.just(it) }
                    }
                    .map {
                        StreamListAction.LoadedArray(it)
                    }
                    .map { it as StreamListAction }
                    .toObservable()
                    .onErrorReturn { error -> StreamListAction.ErrorLoading(error) }
                    .startWith(StreamListAction.LoadData)
            }
    }*/
}