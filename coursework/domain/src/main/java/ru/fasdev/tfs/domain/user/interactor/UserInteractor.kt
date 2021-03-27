package ru.fasdev.tfs.domain.user.interactor

import io.reactivex.rxjava3.core.Single
import ru.fasdev.tfs.domain.model.User
import ru.fasdev.tfs.domain.model.UserStatus

interface UserInteractor {
    fun getAllUsers(): Single<List<User>>
    fun getUserById(id: Int): Single<User>
    fun getStatusUser(id: Int): Single<UserStatus>
    fun getIsOnlineStatusUser(id: Int): Single<Boolean>
    fun searchUser(query: String): Single<List<User>>
}
