package ru.fasdev.tfs.domain.user.repo

import ru.fasdev.tfs.domain.model.User
import ru.fasdev.tfs.domain.model.UserStatus

interface UserRepo
{
    fun getAllUsers(): List<User>
    fun getUserById(id: Int): User?
    fun getStatusUser(id: Int): UserStatus
    fun isOnlineUser(id: Int): Boolean
}