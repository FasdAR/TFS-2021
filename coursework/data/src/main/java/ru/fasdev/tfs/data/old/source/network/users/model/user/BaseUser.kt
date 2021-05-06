package ru.fasdev.tfs.data.old.source.network.users.model.user

interface BaseUser {
    val email: String
    val userId: Long
    val fullName: String
    val avatarUrl: String
}
