package ru.fasdev.tfs.screen.fragment.ownProfile

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.fasdev.tfs.TfsApp
import ru.fasdev.tfs.data.repository.users.UsersRepository
import ru.fasdev.tfs.data.repository.users.UsersRepositoryImpl
import ru.fasdev.tfs.mviCore.MviView
import ru.fasdev.tfs.mviCore.Store
import ru.fasdev.tfs.mviCore.entity.action.Action
import ru.fasdev.tfs.screen.fragment.ownProfile.mvi.OwnProfileAction
import ru.fasdev.tfs.screen.fragment.ownProfile.mvi.OwnProfileState
import javax.inject.Inject

class OwnProfileViewModel @Inject constructor(
    private val usersRepository: UsersRepository
) : ViewModel() {

    private val store: Store<Action, OwnProfileState> = Store(
        initialState = OwnProfileState(),
        reducer = ::reducer,
        middlewares = listOf(::sideActionLoadOwnUser)
    )

    private val wiring = store.wire { actions -> actions.accept(OwnProfileAction.Ui.LoadUser) }
    private var viewBinding: Disposable = Disposables.empty()

    override fun onCleared() {
        super.onCleared()
        wiring.dispose()
    }

    fun bind(view: MviView<Action, OwnProfileState>) {
        viewBinding = store.bind(view)
    }

    fun unBind() {
        viewBinding.dispose()
    }

    private fun reducer(state: OwnProfileState, action: Action): OwnProfileState {
        return when (action) {
            is OwnProfileAction.Internal.LoadedError -> state.copy(
                isLoading = false,
                error = action.error
            )
            is OwnProfileAction.Internal.LoadedUser -> state.copy(
                isLoading = false,
                error = null,
                user = action.user
            )
            is OwnProfileAction.Internal.LoadingUser -> state.copy(isLoading = true, error = null)
            else -> state
        }
    }

    private fun sideActionLoadOwnUser(
        actions: Observable<Action>,
        state: Observable<OwnProfileState>
    ): Observable<Action> {
        return actions
            .ofType(OwnProfileAction.Ui.LoadUser.javaClass)
            .observeOn(Schedulers.io())
            .flatMap { _ ->
                usersRepository.getOwnUser()
                    .toObservable()
                    .map<OwnProfileAction.Internal> { OwnProfileAction.Internal.LoadedUser(it) }
                    .onErrorReturn { OwnProfileAction.Internal.LoadedError(it) }
                    .startWith(OwnProfileAction.Internal.LoadingUser)
            }
    }
}