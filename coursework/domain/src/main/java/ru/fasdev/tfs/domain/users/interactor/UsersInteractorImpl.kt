package ru.fasdev.tfs.domain.users.interactor

import ru.fasdev.tfs.domain.model.User
import ru.fasdev.tfs.domain.users.repo.UsersRepo

class UsersInteractorImpl(private val usersRepo: UsersRepo) : UsersInteractor
{
    override fun getAllUsers(): List<User> = usersRepo.getAllUsers()

    override fun searchUser(query: String): List<User> = getAllUsers()
            .filter {
                val resQuery = query.toLowerCase().trim()

                it.fullName.toLowerCase().contains(resQuery)
                        || it.email.toLowerCase().contains(resQuery)
            }
}