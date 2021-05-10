package ru.fasdev.tfs.data.source.network.users.model

interface BaseUser
{
    val userId: Long
    val email: String
    val fullName: String
    val avatarUrl: String
}