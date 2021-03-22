package ru.fasdev.tfs.domain.user.repo

import ru.fasdev.tfs.domain.model.User

interface UserRepo
{
    fun getAllUsers(): List<User>
    fun getUserById(id: Int): User?
    fun getStatusUser(id: Int): Int
    fun isOnlineUser(id: Int): Boolean
}