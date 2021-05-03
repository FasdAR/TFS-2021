package ru.fasdev.tfs.screen.fragment.people

import androidx.lifecycle.ViewModel
import com.freeletics.rxredux.StateAccessor
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import io.reactivex.Observable.fromIterable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import ru.fasdev.tfs.TfsApp
import ru.fasdev.tfs.data.mapper.toUserUi
import ru.fasdev.tfs.di.module.UserDomainModule
import ru.fasdev.tfs.domain.user.interactor.UserInteractorImpl
import ru.fasdev.tfs.domain.user.model.User
import ru.fasdev.tfs.screen.fragment.people.mvi.PeopleAction
import ru.fasdev.tfs.screen.fragment.people.mvi.PeopleState
import ru.fasdev.tfs.screen.fragment.people.recycler.viewType.UserUi
import ru.fasdev.tfs.view.MviView
import java.util.concurrent.TimeUnit

class PeopleViewModel : ViewModel()
{
    object PeopleComponent {
        val userRepo = UserDomainModule.getUserRepo(TfsApp.AppComponent.userApi)
        val userInteractor = UserInteractorImpl(userRepo)
    }

    private val usersInteractor = PeopleComponent.userInteractor

    private var disposable: CompositeDisposable = CompositeDisposable()

    private val inputRelay: Relay<PeopleAction> = PublishRelay.create()
    val input: Consumer<PeopleAction> get() = inputRelay

    private val store = inputRelay.reduxStore(
        initialState = PeopleState(),
        sideEffects = listOf(::loadAllUsersSideEffect, ::searchUsersSideEffect),
        reducer = ::reducer
    )

    fun attachView(mviView: MviView<PeopleState>) {
        disposable += store.subscribe { mviView.render(it) }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    private fun reducer(state: PeopleState, action: PeopleAction): PeopleState {
        return when(action) {
            is PeopleAction.LoadUsers -> state.copy(isLoading = true, error = null)
            is PeopleAction.LoadedUsers -> state.copy(isLoading = false, error = null, users = action.users)
            is PeopleAction.ErrorLoading -> state.copy(isLoading = false, error = action.error)
            else -> state
        }
    }

    private fun Single<List<User>>.mapToUiUser(): Single<List<UserUi>> {
        return flatMapObservable(::fromIterable)
            .concatMap {
                //Delay for query
                Observable.just(it).delay(10, TimeUnit.MILLISECONDS)
            }
            .flatMapSingle { user ->
                usersInteractor.getStatusUser(user.email)
                    .map { status -> user.toUserUi(status) }
                    .subscribeOn(Schedulers.io())
            }
            .toList()
    }

    private fun loadAllUsersSideEffect(actions: Observable<PeopleAction>, state: StateAccessor<PeopleState>): Observable<PeopleAction>
    {
        return actions
            .ofType(PeopleAction.SideEffectLoadUsers::class.java)
            .switchMap {
                usersInteractor.getAllUsers()
                    .subscribeOn(Schedulers.io())
                    .mapToUiUser()
                    .toObservable()
                    .map { PeopleAction.LoadedUsers(it) }
                    .map { it as PeopleAction}
                    .onErrorReturn { error -> PeopleAction.ErrorLoading(error) }
                    .startWith(PeopleAction.LoadUsers)
            }
    }

    private fun searchUsersSideEffect(actions: Observable<PeopleAction>, state: StateAccessor<PeopleState>): Observable<PeopleAction>
    {
        return actions
            .ofType(PeopleAction.SideEffectSearchUsers::class.java)
            .debounce(1000, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .switchMap {
                val source = if (it.query.isNotEmpty()) {
                    usersInteractor.searchUser(it.query)
                } else {
                    usersInteractor.getAllUsers()
                }

                source
                    .subscribeOn(Schedulers.io())
                    .mapToUiUser()
                    .toObservable()
                    .map { PeopleAction.LoadedUsers(it) }
                    .map { it as PeopleAction}
                    .onErrorReturn { error -> PeopleAction.ErrorLoading(error) }
                    .startWith(PeopleAction.LoadUsers)
            }
    }
}