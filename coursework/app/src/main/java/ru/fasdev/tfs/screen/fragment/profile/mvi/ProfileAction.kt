package ru.fasdev.tfs.screen.fragment.profile.mvi

import ru.fasdev.tfs.domain.user.model.UserStatus

sealed class ProfileAction
{
    object LoadUser : ProfileAction()
    class ErrorLoading(val error: Throwable) : ProfileAction()
    class LoadedUser(val userFullName: String, val userAvatar: String, val userStatus: UserStatus) : ProfileAction()
}