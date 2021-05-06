package ru.fasdev.tfs.screen.fragment.profileAnother

import androidx.lifecycle.ViewModel
import com.freeletics.rxredux.StateAccessor
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import ru.fasdev.tfs.TfsApp
import ru.fasdev.tfs.data.old.mapper.toUserUi
import ru.fasdev.tfs.di.module.UserDomainModule
import ru.fasdev.tfs.domain.old.user.interactor.UserInteractor
import ru.fasdev.tfs.domain.old.user.interactor.UserInteractorImpl
import ru.fasdev.tfs.screen.fragment.profileAnother.mvi.ProfileAnotherAction
import ru.fasdev.tfs.screen.fragment.profileAnother.mvi.ProfileAnotherState
import ru.fasdev.tfs.view.MviView

class ProfileAnotherViewModel : ViewModel()
{
    object ProfileAnotherComponent {
        val userRepo = UserDomainModule.getUserRepo(TfsApp.AppComponent.userApi)
        val userInteractor = UserInteractorImpl(userRepo)
    }

    private val userInteractor: UserInteractor = ProfileAnotherComponent.userInteractor

    private val compositeDisposable = CompositeDisposable()
    private val inputRelay: Relay<ProfileAnotherAction> = PublishRelay.create()
    val input: Consumer<ProfileAnotherAction> get() = inputRelay

    val store = inputRelay.reduxStore(
        initialState = ProfileAnotherState(),
        sideEffects = listOf(::loadUserSideEffect),
        reducer = ::reducer
    )

    fun attachView(mviView: MviView<ProfileAnotherState>) {
        compositeDisposable += store.observeOn(AndroidSchedulers.mainThread()).subscribe { mviView.render(it) }
    }

    fun reducer(state: ProfileAnotherState, action: ProfileAnotherAction): ProfileAnotherState {
        return when (action) {
            is ProfileAnotherAction.ErrorLoading -> state.copy(isLoading = false, error = action.error)
            is ProfileAnotherAction.LoadUser -> state.copy(isLoading = true, error = null)
            is ProfileAnotherAction.LoadedUser -> state.copy(isLoading = false, error = null,
                userAvatar = action.userAvatar, userFullName = action.userFullName, userStatus = action.userStatus)
            else -> state
        }
    }

    fun loadUserSideEffect(action: Observable<ProfileAnotherAction>, state: StateAccessor<ProfileAnotherState>): Observable<ProfileAnotherAction> {
        return action
            .ofType(ProfileAnotherAction.SideEffectLoadUser::class.java)
            .switchMap {
                userInteractor
                    .getUserById(it.idUser)
                    .subscribeOn(Schedulers.io())
                    .flatMap { user ->
                        userInteractor
                            .getStatusUser(user.email)
                            .map { status ->
                                user.toUserUi(status)
                            }
                    }
                    .toObservable()
                    .map { userUi ->
                        ProfileAnotherAction.LoadedUser(
                            userFullName = userUi.fullName,
                            userStatus = userUi.userStatus,
                            userAvatar = userUi.avatarSrc
                        )
                    }
                    .map { it as ProfileAnotherAction }
                    .onErrorReturn { error -> ProfileAnotherAction.ErrorLoading(error)}
                    .startWith(ProfileAnotherAction.LoadUser)
            }
    }
}