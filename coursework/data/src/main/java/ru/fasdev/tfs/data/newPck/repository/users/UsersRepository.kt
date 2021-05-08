package ru.fasdev.tfs.data.newPck.repository.users

import io.reactivex.Single
import ru.fasdev.tfs.domain.newPck.user.model.User
import ru.fasdev.tfs.domain.newPck.user.model.UserOnlineStatus

interface UsersRepository
{
    fun getOwnUser(): Single<User>
    fun getStatusUser(email: String): Single<UserOnlineStatus>
    fun getAllUsers(): Single<List<User>>
}