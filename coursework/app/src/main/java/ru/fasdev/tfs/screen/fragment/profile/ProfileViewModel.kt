package ru.fasdev.tfs.screen.fragment.profile

import androidx.lifecycle.ViewModel
import ru.fasdev.tfs.TfsApp
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

    /*

    val store = Store(
        initState = ProfileState(),
        middleware = this,
        reducer = this
    )

    init {
        store.procession(ProfileAction.LoadUser)
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

    override fun middleware(action: ProfileAction, state: ProfileState): ProfileAction {
        return when (action) {
            ProfileAction.LoadUser -> {
                ProfileAction.LoadedUser(
                    userFullName = "USER USEROVICH",
                    userAvatar = "",
                    userStatus = UserStatus.OFFLINE
                )
            }
            else -> action
        }
    }*/
}