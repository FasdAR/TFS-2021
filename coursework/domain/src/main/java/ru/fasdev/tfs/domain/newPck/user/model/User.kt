package ru.fasdev.tfs.domain.newPck.user.model

data class User(
    val id: Long,
    val avatarUrl: String,
    val fullName: String,
    val email: String,
    val onlineStatus: UserOnlineStatus = UserOnlineStatus.OFFLINE,
    val isBot: Boolean = true
)