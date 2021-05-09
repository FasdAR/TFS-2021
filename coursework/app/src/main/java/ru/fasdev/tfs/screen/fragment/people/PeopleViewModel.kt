package ru.fasdev.tfs.screen.fragment.people

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.fasdev.tfs.TfsApp
import ru.fasdev.tfs.data.mapper.toUserItem
import ru.fasdev.tfs.data.newPck.repository.users.UsersRepositoryImpl
import ru.fasdev.tfs.mviCore.MviView
import ru.fasdev.tfs.mviCore.Store
import ru.fasdev.tfs.mviCore.entity.action.Action
import ru.fasdev.tfs.recycler.base.viewHolder.ViewType
import ru.fasdev.tfs.recycler.item.emptySearch.EmptySearchItem
import ru.fasdev.tfs.recycler.item.user.UserItem
import ru.fasdev.tfs.screen.fragment.people.mvi.PeopleAction
import ru.fasdev.tfs.screen.fragment.people.mvi.PeopleState
import java.util.concurrent.TimeUnit

class PeopleViewModel : ViewModel() {
    private companion object {
        const val SEARCH_TIME_OUT = 500L
    }

    //#region Test Di
    object PeopleComponent {
        val usersRepository = UsersRepositoryImpl(TfsApp.AppComponent.newUserApi)
    }
    //#endregion

    private val store: Store<Action, PeopleState> = Store(
        initialState = PeopleState(),
        reducer = ::reducer,
        middlewares = listOf(::sideActionLoadUsers, ::sideActionSearchUsers)
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
                            .map { user -> user.toUserItem() }
                            .toList()
                    }
                    .toObservable()
                    .map<PeopleAction.Internal> { PeopleAction.Internal.LoadedUsers(it) }
                    .onErrorReturn { PeopleAction.Internal.LoadedError(it) }
                    .startWith(PeopleAction.Internal.LoadingUsers)
            }
    }

    private fun sideActionSearchUsers(
        actions: Observable<Action>,
        state: Observable<PeopleState>
    ): Observable<Action> {
        return actions
            .ofType(PeopleAction.Ui.SearchUsers::class.java)
            .debounce(SEARCH_TIME_OUT, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .observeOn(Schedulers.io())
            .switchMap { action ->
                PeopleComponent.usersRepository.searchUsers(action.query)
                    .flatMap { list ->
                        Observable.fromIterable(list)
                            .map<ViewType> { user -> user.toUserItem() }
                            .switchIfEmpty(Observable.just(EmptySearchItem(uId = -1)))
                            .toList()
                    }
                    .toObservable()
                    .map<PeopleAction.Internal> { PeopleAction.Internal.LoadedUsers(it) }
                    .onErrorReturn { PeopleAction.Internal.LoadedError(it) }
            }
    }
}