package ru.fasdev.tfs.screen.fragment.ownProfile.mvi

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign
import ru.fasdev.tfs.data.newPck.repository.users.UsersRepository
import ru.fasdev.tfs.mviCore.Middleware
import ru.fasdev.tfs.mviCore.MviView
import ru.fasdev.tfs.mviCore.Reducer
import ru.fasdev.tfs.mviCore.Store
import ru.fasdev.tfs.mviCore.entity.action.Action
import ru.fasdev.tfs.screen.fragment.ownProfile.mvi.middleware.LoadUserMiddleware

class OwnProfileStore(private val usersRepository: UsersRepository): Store<Action, OwnProfileState>
{
    override val initialState: OwnProfileState = OwnProfileState()
    override val reducer: Reducer<OwnProfileState, Action> = OwnProfileReducer()
    override val middlewares: List<Middleware<Action, OwnProfileState>> = listOf(LoadUserMiddleware(usersRepository))

    private val state = BehaviorRelay.createDefault(initialState)
    private val actions = PublishRelay.create<Action>()

    fun wire(): Disposable {
        val disposable = CompositeDisposable()

        disposable += actions
            .withLatestFrom(state) { action, state ->
                reducer.reduce(state, action)
            }
            .distinctUntilChanged()
            .subscribe(state::accept)

        disposable += Observable.merge<Action> {
            middlewares.map { it.handle(actions, state) }
        }.subscribe(actions::accept)

        return disposable
    }

    fun bind(view: MviView<Action, OwnProfileState>): Disposable {
        val disposable = CompositeDisposable()

        disposable += state.observeOn(AndroidSchedulers.mainThread()).subscribe(view::render)
        disposable += view.actions.subscribe(actions::accept)

        return disposable
    }
}