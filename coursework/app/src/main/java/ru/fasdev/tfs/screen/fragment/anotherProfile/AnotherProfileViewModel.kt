package ru.fasdev.tfs.screen.fragment.anotherProfile

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.fasdev.tfs.TfsApp
import ru.fasdev.tfs.data.repository.users.UsersRepositoryImpl
import ru.fasdev.tfs.mviCore.MviView
import ru.fasdev.tfs.mviCore.Store
import ru.fasdev.tfs.mviCore.entity.action.Action
import ru.fasdev.tfs.screen.fragment.anotherProfile.mvi.AnotherProfileAction
import ru.fasdev.tfs.screen.fragment.anotherProfile.mvi.AnotherProfileState

class AnotherProfileViewModel : ViewModel() {
    //#region Test DI
    object ProfileComponent {
       val usersRepository = UsersRepositoryImpl(TfsApp.AppComponent.newUserApi)
    }
    //#ednregion

    private val store: Store<Action, AnotherProfileState> = Store(
        initialState = AnotherProfileState(),
        reducer = ::reducer,
        middlewares = listOf(::sideActionLoadUser)
    )

    private val wiring = store.wire()
    private var viewBinding: Disposable = Disposables.empty()

    override fun onCleared() {
        super.onCleared()
        wiring.dispose()
    }

    fun bind(view: MviView<Action, AnotherProfileState>) {
        viewBinding = store.bind(view)
    }

    fun unBind() {
        viewBinding.dispose()
    }

    private fun reducer(state: AnotherProfileState, action: Action): AnotherProfileState {
        return when (action) {
            is AnotherProfileAction.Internal.LoadedError -> state.copy(
                isLoading = false,
                error = action.error
            )
            is AnotherProfileAction.Internal.LoadedUser -> state.copy(
                isLoading = false,
                error =  null,
                user = action.user
            )
            is AnotherProfileAction.Internal.LoadingUser -> state.copy(isLoading = true, error = null)
            else -> state
        }
    }

    private fun sideActionLoadUser(
        actions: Observable<Action>,
        state: Observable<AnotherProfileState>
    ): Observable<Action> {
        return actions
            .ofType(AnotherProfileAction.Ui.LoadUser::class.java)
            .observeOn(Schedulers.io())
            .flatMap { action ->
                ProfileComponent.usersRepository.getUserById(action.id)
                    .toObservable()
                    .map<AnotherProfileAction.Internal> { AnotherProfileAction.Internal.LoadedUser(it) }
                    .onErrorReturn { AnotherProfileAction.Internal.LoadedError(it) }
                    .startWith(AnotherProfileAction.Internal.LoadingUser)
            }
    }
}