package ru.fasdev.tfs.domain.users.interactor

import ru.fasdev.tfs.domain.model.User

interface UsersInteractor
{
    fun getAllUsers(): List<User>
    fun searchUser(query: String): List<User>
}