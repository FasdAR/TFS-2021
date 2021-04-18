package ru.fasdev.tfs.mvi

import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import ru.fasdev.tfs.mvi.action.Action
import ru.fasdev.tfs.mvi.state.State

class Store<S : State, A : Action>(
    initState: S,
    private val actions: PublishSubject<A>,
    private val middlewares: Middleware<S, A>,
    private val reducer: Reducer<S, A>
) {
    var state: BehaviorSubject<S> = BehaviorSubject.create()

    private val currentState: S get() = state.value

    init {
        state.onNext(initState)

        actions
            .subscribeOn(Schedulers.io())
            .doOnNext { procession(it) }
            .subscribeBy()
    }

    private fun procession(action: A) {
        val newState = reducer.reduce(action, currentState)
        state.onNext(newState)

        middlewares.middleware(action, currentState)
    }
}