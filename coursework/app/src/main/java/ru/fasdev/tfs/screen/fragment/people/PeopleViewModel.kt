package ru.fasdev.tfs.screen.fragment.people

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.fasdev.tfs.TfsApp
import ru.fasdev.tfs.data.newPck.repository.users.UsersRepositoryImpl
import ru.fasdev.tfs.mviCore.MviView
import ru.fasdev.tfs.mviCore.Store
import ru.fasdev.tfs.mviCore.entity.action.Action
import ru.fasdev.tfs.recycler.item.user.UserItem
import ru.fasdev.tfs.screen.fragment.people.mvi.PeopleAction
import ru.fasdev.tfs.screen.fragment.people.mvi.PeopleState

class PeopleViewModel : ViewModel()
{
    //#region Test Di
    object PeopleComponent {
        val usersRepository = UsersRepositoryImpl(TfsApp.AppComponent.newUserApi)
    }
    //#endregion

    private val store: Store<Action, PeopleState> = Store(
        initialState = PeopleState(),
        reducer = ::reducer,
        middlewares = listOf(::sideActionLoadUsers)
    )

    private val wiring = store.wire { actionsFlow -> actionsFlow.accept(PeopleAction.Ui.LoadUsers) }
    private var viewBinding: Disposable = Disposables.empty()

    override fun onCleared() {
        super.onCleared()
        wiring.dispose()
    }

    fun bind(view: MviView<Action, PeopleState>) {
        viewBinding = store.bind(view)
    }

    fun unBind() {
        viewBinding.dispose()
    }

    private fun reducer(state: PeopleState, action: Action): PeopleState {
        return when (action) {
            is PeopleAction.Internal.LoadedUsers -> state.copy(
                isLoading = false,
                error = null,
                users = action.users
            )
            is PeopleAction.Internal.LoadedError -> state.copy(
                isLoading = false,
                error = action.error,
                users = emptyList()
            )
            is PeopleAction.Internal.LoadingUsers -> state.copy(
                isLoading = true,
                error = null,
            )
            else -> state
        }
    }

    private fun sideActionLoadUsers(
        actions: Observable<Action>,
        state: Observable<PeopleState>
    ): Observable<Action> {
        return actions
            .ofType(PeopleAction.Ui.LoadUsers.javaClass)
            .observeOn(Schedulers.io())
            .flatMap { _ ->
                PeopleComponent.usersRepository.getAllUsers()
                    .flatMap {
                        Observable.fromIterable(it)
                            .map { user ->
                                UserItem(
                                    uId = user.id.toInt(),
                                    avatarSrc = user.avatarUrl,
                                    fullName = user.fullName,
                                    email = user.email,
                                    userStatus = user.onlineStatus,
                                )
                            }
                            .toList()
                    }
                    .toObservable()
                    .map<PeopleAction.Internal> { PeopleAction.Internal.LoadedUsers(it) }
                    .onErrorReturn { PeopleAction.Internal.LoadedError(it) }
                    .startWith (PeopleAction.Internal.LoadingUsers)
            }
    }

    private fun sideActionSearchUsers(
        actions: Observable<Action>,
        state: Observable<PeopleState>
    ): Observable<Action> {
        return actions
            .ofType(PeopleAction.Ui.SearchUsers::class.java)
            .observeOn(Schedulers.io())
            .flatMap { _ ->
                TODO("ADD FLAT MAP")
            }
    }

    /*
    private fun Single<List<User>>.mapToUiUser(): Single<List<UserItem>> {
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
    }*/
}