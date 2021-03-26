package ru.fasdev.tfs.domain.user.interactor

import ru.fasdev.tfs.domain.model.User
import ru.fasdev.tfs.domain.model.UserStatus
import ru.fasdev.tfs.domain.user.repo.UserRepo

class UserInteractorImpl(private val usersRepo: UserRepo) : UserInteractor {
    override fun getAllUsers(): List<User> = usersRepo.getAllUsers()
    override fun getUserById(id: Int): User? = usersRepo.getUserById(id)
    override fun getStatusUser(id: Int): UserStatus = usersRepo.getStatusUser(id)
    override fun getIsOnlineStatusUser(id: Int): Boolean = usersRepo.isOnlineUser(id)

    override fun searchUser(query: String): List<User> {
        return getAllUsers().filter {
            val resQuery = query.toLowerCase().trim()
            it.fullName.toLowerCase().contains(resQuery) || it.email.toLowerCase().contains(resQuery)
        }
    }
}
