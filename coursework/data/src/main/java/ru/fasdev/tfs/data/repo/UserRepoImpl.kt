package ru.fasdev.tfs.data.repo

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.fasdev.tfs.data.mapper.toUserDomain
import ru.fasdev.tfs.data.mapper.toUserStatus
import ru.fasdev.tfs.data.source.network.users.api.UserApi
import ru.fasdev.tfs.domain.user.model.User
import ru.fasdev.tfs.domain.user.model.UserStatus
import ru.fasdev.tfs.domain.user.repo.UserRepo

class UserRepoImpl (private val userApi: UserApi): UserRepo
{
    override fun getAllUsers(): Single<List<User>> {
        return userApi.getAllUsers()
            .flatMapObservable { Observable.fromIterable(it.members) }
            .filter { !it.isBot }
            .map { it.toUserDomain() }
            .toList()
    }

    override fun getUserById(id: Long): Single<User> {
       return userApi.getUserById(id)
           .map { it.user.toUserDomain() }
    }

    override fun getStatusUser(email: String): Single<UserStatus> {
        return userApi.getUserPresence(email)
            .map { it.presence.aggregated.toUserStatus() }
    }
}