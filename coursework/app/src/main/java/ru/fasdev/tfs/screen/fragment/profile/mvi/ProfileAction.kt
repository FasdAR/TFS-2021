package ru.fasdev.tfs.screen.fragment.profile.mvi

import ru.fasdev.tfs.domain.user.model.UserStatus
import ru.fasdev.tfs.mvi.action.Action

sealed class ProfileAction : Action
{
    object LoadUser : ProfileAction()
    class ErrorLoading(val error: Throwable) : ProfileAction()
    class LoadedUser(val userFullName: String, val userAvatar: String, val userStatus: UserStatus) : ProfileAction()
}