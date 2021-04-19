package ru.fasdev.tfs.screen.fragment.profile.mvi

import ru.fasdev.tfs.domain.user.model.UserStatus

data class ProfileState (
    val isLoading: Boolean = false,
    val userFullName: String? = null,
    val userAvatar: String? = null,
    val userStatus: UserStatus = UserStatus.OFFLINE,
    val error: Throwable? = null
)