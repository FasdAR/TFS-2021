package ru.fasdev.tfs.screen.fragment.profileAnother.mvi

import ru.fasdev.tfs.domain.user.model.UserStatus

sealed class ProfileAnotherAction
{
    object LoadUser : ProfileAnotherAction()
    class ErrorLoading(val error: Throwable) : ProfileAnotherAction()
    class LoadedUser(val userFullName: String, val userAvatar: String, val userStatus: UserStatus) : ProfileAnotherAction()

    class SideEffectLoadUser(val idUser: Long): ProfileAnotherAction()
}