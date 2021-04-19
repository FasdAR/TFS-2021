package ru.fasdev.tfs.screen.fragment.profile

import androidx.lifecycle.ViewModel
import com.freeletics.rxredux.StateAccessor
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import ru.fasdev.tfs.TfsApp
import ru.fasdev.tfs.data.mapper.toUserUi
import ru.fasdev.tfs.di.module.UserDomainModule
import ru.fasdev.tfs.domain.user.interactor.UserInteractorImpl
import ru.fasdev.tfs.screen.fragment.profile.mvi.ProfileAction
import ru.fasdev.tfs.screen.fragment.profile.mvi.ProfileState

class ProfileViewModel : ViewModel() {
    object ProfileComponent {
        val userRepo = UserDomainModule.getUserRepo(TfsApp.AppComponent.userApi)
        val userInteractor = UserInteractorImpl(userRepo)
    }

    private val usersInteractor = ProfileComponent.userInteractor

    private val inputRelay: Relay<ProfileAction> = PublishRelay.create()
    val input: Consumer<ProfileAction> get() = inputRelay

    val store = inputRelay.reduxStore(
        initialState = ProfileState(),
        sideEffects = listOf(::loadUserSideEffect),
        reducer = ::reducer
    )

    fun reducer(state: ProfileState, action: ProfileAction): ProfileState
    {
        return when (action) {
            is ProfileAction.ErrorLoading -> state.copy(isLoading = false, error = action.error)
            is ProfileAction.LoadUser -> state.copy(isLoading = true, error = null)
            is ProfileAction.LoadedUser -> state.copy(isLoading = false, error = null,
                userAvatar = action.userAvatar, userFullName = action.userFullName, userStatus = action.userStatus)
            else -> state
        }
    }

    fun loadUserSideEffect(actions: Observable<ProfileAction>, state: StateAccessor<ProfileState>): Observable<ProfileAction> {
        return actions
            .ofType(ProfileAction.SideEffectLoadUser::class.java)
            .switchMap {
                usersInteractor
                    .getOwnUser()
                    .subscribeOn(Schedulers.io())
                    .flatMap { user ->
                        usersInteractor
                            .getStatusUser(user.email)
                            .map { status ->
                                user.toUserUi(status)
                            }
                    }
                    .toObservable()
                    .map { userUi ->
                        ProfileAction.LoadedUser(
                            userFullName = userUi.fullName,
                            userStatus = userUi.userStatus,
                            userAvatar = userUi.avatarSrc
                        )
                    }
                    .map { it as ProfileAction }
                    .onErrorReturn { error -> ProfileAction.ErrorLoading(error)}
                    .startWith(ProfileAction.LoadUser)
            }
    }
}