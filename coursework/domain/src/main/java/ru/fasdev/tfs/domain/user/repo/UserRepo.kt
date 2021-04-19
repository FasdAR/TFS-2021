package ru.fasdev.tfs.domain.user.repo

import io.reactivex.Single
import ru.fasdev.tfs.domain.user.model.User
import ru.fasdev.tfs.domain.user.model.UserStatus

interface UserRepo {
    fun getAllUsers(): Single<List<User>>
    fun getUserById(id: Long): Single<User>
    fun getStatusUser(email: String): Single<UserStatus>
    fun getOwnUser(): Single<User>
}
