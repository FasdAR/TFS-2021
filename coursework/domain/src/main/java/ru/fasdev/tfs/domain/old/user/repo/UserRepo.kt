package ru.fasdev.tfs.domain.old.user.repo

import io.reactivex.Single
import ru.fasdev.tfs.domain.old.user.model.User
import ru.fasdev.tfs.domain.old.user.model.UserStatus

interface UserRepo {
    fun getAllUsers(): Single<List<User>>
    fun getUserById(id: Long): Single<User>
    fun getStatusUser(email: String): Single<UserStatus>
    fun getOwnUser(): Single<User>
}
