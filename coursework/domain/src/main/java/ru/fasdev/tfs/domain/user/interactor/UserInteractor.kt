package ru.fasdev.tfs.domain.user.interactor

import io.reactivex.Single
import ru.fasdev.tfs.domain.user.model.User
import ru.fasdev.tfs.domain.user.model.UserStatus

interface UserInteractor {
    fun getAllUsers(): Single<List<User>>
    fun getUserById(id: Long): Single<User>
    fun getStatusUser(email: String): Single<UserStatus>
    fun searchUser(query: String): Single<List<User>>
}
