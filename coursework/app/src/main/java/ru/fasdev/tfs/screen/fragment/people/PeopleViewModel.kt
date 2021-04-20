package ru.fasdev.tfs.screen.fragment.people

import androidx.lifecycle.ViewModel
import com.freeletics.rxredux.StateAccessor
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import io.reactivex.Observable.fromIterable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import ru.fasdev.tfs.TfsApp
import ru.fasdev.tfs.data.mapper.toUserUi
import ru.fasdev.tfs.di.module.UserDomainModule
import ru.fasdev.tfs.domain.user.interactor.UserInteractorImpl
import ru.fasdev.tfs.domain.user.model.User
import ru.fasdev.tfs.screen.fragment.people.mvi.PeopleAction
import ru.fasdev.tfs.screen.fragment.people.mvi.PeopleState
import ru.fasdev.tfs.screen.fragment.people.recycler.viewType.UserUi
import ru.fasdev.tfs.screen.fragment.profile.mvi.ProfileAction
import ru.fasdev.tfs.screen.fragment.profile.mvi.ProfileState
import java.util.concurrent.TimeUnit

class PeopleViewModel : ViewModel()
{
    object PeopleComponent {
        val userRepo = UserDomainModule.getUserRepo(TfsApp.AppComponent.userApi)
        val userInteractor = UserInteractorImpl(userRepo)
    }

    private val usersInteractor = PeopleComponent.userInteractor

    private val inputRelay: Relay<ProfileAction> = PublishRelay.create()
    val input: Consumer<ProfileAction> get() = inputRelay

    val store = inputRelay.reduxStore(
        initialState = PeopleState(),
        sideEffects = listOf(::loadAllUsersSideEffect),
        reducer = ::reducer
    )

    private val compositeDisposable = CompositeDisposable()

    fun reducer(state: PeopleState, action: PeopleAction): PeopleState {
        return when(action) {
            PeopleAction.LoadUsers -> state.copy(isLoading = true, error = null)
            else -> state
        }
    }

    private fun Single<List<User>>.mapToUiUser(): Single<List<UserUi>> {
        return flatMapObservable(::fromIterable)
            .concatMap {
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

    /*
    private val searchSubject = PublishSubject.create<String>()

    fun searchUser(query: String) {

    }*/
}

/*
// #region Rx chains
private fun searchUser(query: String = "") {
    searchSubject.onNext(query)
}

private fun Single<List<User>>.mapToUiUser(): Single<List<UserUi>> {
    return flatMapObservable(Observable::fromIterable)
        .concatMap {
            Observable.just(it).delay(10, TimeUnit.MILLISECONDS)
        }
        .flatMapSingle { user ->
            usersInteractor.getStatusUser(user.email)
                .map { status -> user.toUserUi(status) }
                .subscribeOn(Schedulers.io())
        }
        .toList()
}

private fun loadAllUsers() {
    compositeDisposable.add(
        usersInteractor.getAllUsers()
            .subscribeOn(Schedulers.io())
            .mapToUiUser()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { array ->
                    adapter.items = array
                },
                onError = ::onError
            )
    )
}

private fun observerSearch() {
    compositeDisposable.add(
        searchSubject
            .debounce(500, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .switchMapSingle {
                if (it.isNotEmpty()) usersInteractor.searchUser(it)
                else usersInteractor.getAllUsers()
            }
            .flatMapSingle {
                Single.just(it)
                    .mapToUiUser()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorReturn { error ->
                onError(error)
                return@onErrorReturn listOf()
            }
            .subscribeBy(
                onNext = { array ->
                    adapter.items = array
                }
            )
    )
}
// #endregion
 */