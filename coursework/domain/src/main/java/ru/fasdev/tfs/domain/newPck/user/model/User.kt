package ru.fasdev.tfs.domain.newPck.user.model

data class User(
    val id: Long,
    val avatarUrl: String? = null,
    val fullName: String,
    val email: String? = null,
    val onlineStatus: UserOnlineStatus = UserOnlineStatus.OFFLINE
)