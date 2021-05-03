package ru.fasdev.tfs.screen.fragment.streamList

import android.util.Log
import androidx.lifecycle.ViewModel
import com.freeletics.rxredux.StateAccessor
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Observable.fromIterable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import ru.fasdev.tfs.TfsApp
import ru.fasdev.tfs.data.mapper.toStreamUi
import ru.fasdev.tfs.data.mapper.toTopicUi
import ru.fasdev.tfs.di.module.StreamDomainModule
import ru.fasdev.tfs.domain.stream.interactor.StreamInteractor
import ru.fasdev.tfs.domain.stream.model.Stream
import ru.fasdev.tfs.screen.fragment.streamList.mvi.StreamListAction
import ru.fasdev.tfs.screen.fragment.streamList.mvi.StreamListState
import ru.fasdev.tfs.screen.fragment.streamList.recycler.viewType.StreamUi
import ru.fasdev.tfs.view.MviView
import java.util.concurrent.TimeUnit

class StreamListViewModel : ViewModel() {
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

    private fun Flowable<List<Stream>>.mapToDomain(): Flowable<List<StreamUi>> {
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
                    state.itemsList.filter { it is StreamUi }.map { it as StreamUi }
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
                                    currentArray.indexOfFirst { it.uId == action.idStream && it is StreamUi }
                                val stream = currentArray[currentStreamIndex] as StreamUi
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
    }
}