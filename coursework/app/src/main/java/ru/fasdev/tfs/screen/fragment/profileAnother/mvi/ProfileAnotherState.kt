package ru.fasdev.tfs.screen.fragment.profileAnother.mvi

import ru.fasdev.tfs.domain.old.user.model.UserStatus

data class ProfileAnotherState (
    val isLoading: Boolean = false,
    val userFullName: String? = null,
    val userAvatar: String? = null,
    val userStatus: UserStatus = UserStatus.OFFLINE,
    val error: Throwable? = null
)