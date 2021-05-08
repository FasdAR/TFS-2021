package ru.fasdev.tfs.mviCore

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import ru.fasdev.tfs.mviCore.entity.action.Action
import ru.fasdev.tfs.mviCore.entity.state.UiState

open class Store<A : Action, S : UiState>(
    initialState: S,
    private val reducer: Reducer<S, A>,
    private val middlewares: List<Middleware<A, S>>
) {
    private val stateFlow = BehaviorRelay.createDefault(initialState)
    private val actionsFlow = PublishRelay.create<A>()

    fun wire(startActions: (actionsFlow: PublishRelay<A>) -> Unit = {}): Disposable {
        return CompositeDisposable().apply {
            add(
                actionsFlow
                    .withLatestFrom(stateFlow) { action, state ->
                        reducer(state, action)
                    }
                    .distinctUntilChanged()
                    .subscribe(stateFlow::accept)
            )
            add(
                actionsFlow
                    .publish { published ->
                        Observable.merge(
                            middlewares.map { it(published, stateFlow) }
                        )
                    }
                    .subscribe(actionsFlow::accept)
            )

            startActions(actionsFlow)
        }
    }

    fun bind(view: MviView<A, S>): Disposable {
        return CompositeDisposable().apply {
            add(
                stateFlow
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(view::render)
            )
            add(
                view.actions
                    .subscribe(actionsFlow::accept)
            )
        }
    }
}