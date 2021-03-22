package ru.fasdev.tfs.domain.users.interactor

import ru.fasdev.tfs.domain.model.User

interface UsersInteractor
{
    fun getAllUsers(): List<User>
    fun getUserById(id: Int): User?
    fun getStatusUser(id: Int): Int
    fun isOnlineUser(id: Int): Boolean
    fun searchUser(query: String): List<User>
}