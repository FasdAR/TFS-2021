package ru.fasdev.tfs.domain.user.interactor

import ru.fasdev.tfs.domain.model.User
import ru.fasdev.tfs.domain.model.UserStatus

interface UserInteractor {
    fun getAllUsers(): List<User>
    fun getUserById(id: Int): User?
    fun getStatusUser(id: Int): UserStatus
    fun isOnlineUser(id: Int): Boolean
    fun searchUser(query: String): List<User>
}
