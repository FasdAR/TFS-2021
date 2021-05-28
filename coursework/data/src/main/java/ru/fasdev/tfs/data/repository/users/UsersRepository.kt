package ru.fasdev.tfs.data.repository.users

import io.reactivex.Single
import ru.fasdev.tfs.domain.user.model.User
import ru.fasdev.tfs.domain.user.model.UserOnlineStatus

interface UsersRepository {
    fun getOwnUser(): Single<User>
    fun getStatusUser(email: String): Single<UserOnlineStatus>
    fun getAllUsers(): Single<List<User>>
    fun searchUsers(query: String): Single<List<User>>
    fun getUserById(id: Long): Single<User>
}
