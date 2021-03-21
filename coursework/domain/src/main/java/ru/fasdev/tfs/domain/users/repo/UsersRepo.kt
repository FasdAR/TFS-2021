package ru.fasdev.tfs.domain.users.repo

import ru.fasdev.tfs.domain.model.User

interface UsersRepo
{
    fun getAllUsers(): List<User>
}