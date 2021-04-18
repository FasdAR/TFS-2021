package ru.fasdev.tfs.screen.fragment.profile

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import ru.fasdev.tfs.TfsApp
import ru.fasdev.tfs.di.module.UserDomainModule
import ru.fasdev.tfs.domain.user.interactor.UserInteractorImpl
import ru.fasdev.tfs.mvi.Middleware
import ru.fasdev.tfs.mvi.Reducer
import ru.fasdev.tfs.mvi.Store
import ru.fasdev.tfs.screen.fragment.profile.mvi.ProfileAction
import ru.fasdev.tfs.screen.fragment.profile.mvi.ProfileState

class ProfileViewModel : ViewModel(), Reducer<ProfileState, ProfileAction>,
    Middleware<ProfileState, ProfileAction> {
    object ProfileComponent {
        val userRepo = UserDomainModule.getUserRepo(TfsApp.AppComponent.userApi)
        val userInteractor = UserInteractorImpl(userRepo)
    }

    private val usersInteractor = ProfileComponent.userInteractor

    val actions: PublishSubject<ProfileAction> = PublishSubject.create()

    private val store = Store(
        initState = ProfileState(),
        actions = actions,
        middlewares = this,
        reducer = this
    )

    val state = store.state

    init {
        actions.onNext(ProfileAction.LoadUser)
    }

    override fun reduce(action: ProfileAction, state: ProfileState): ProfileState {
        return when (action) {
            is ProfileAction.ErrorLoading -> state.copy(isLoading = false, error = action.error)
            is ProfileAction.LoadUser -> state.copy(isLoading = true, error = null)
            is ProfileAction.LoadedUser -> state.copy(
                isLoading = false,
                userFullName = state.userFullName,
                userAvatar = state.userAvatar,
                userStatus = state.userStatus,
                error = null
            )
        }
    }

    override fun middleware(action: ProfileAction, state: ProfileState) {
        when (action) {
            ProfileAction.LoadUser -> {
                usersInteractor.getOwnUser()
                    .flatMap { user ->
                        usersInteractor.getStatusUser(user.email)
                            .map { status ->
                                ProfileAction.LoadedUser(
                                    userFullName = user.fullName,
                                    userAvatar = user.avatarUrl,
                                    userStatus = status
                                )
                            }
                    }
                    .subscribeBy(
                        onSuccess = {
                            actions.onNext(it)
                        },
                        onError = {
                            actions.onNext(ProfileAction.ErrorLoading(it))
                        }
                    )
            }
        }
    }
}