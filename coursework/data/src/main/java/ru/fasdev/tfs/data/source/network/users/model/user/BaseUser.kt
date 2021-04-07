package ru.fasdev.tfs.data.source.network.users.model.user

interface BaseUser {
    val email: String
    val userId: Long
    val fullName: String
    val avatarUrl: String
}