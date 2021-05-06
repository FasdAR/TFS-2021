package ru.fasdev.tfs.data.old.repo

import io.reactivex.Observable
import io.reactivex.Single
import ru.fasdev.tfs.data.old.mapper.toUserDomain
import ru.fasdev.tfs.data.old.mapper.toUserStatus
import ru.fasdev.tfs.data.old.source.network.users.api.UserApi
import ru.fasdev.tfs.domain.old.user.model.User
import ru.fasdev.tfs.domain.old.user.model.UserStatus
import ru.fasdev.tfs.domain.old.user.repo.UserRepo

class UserRepoImpl(private val userApi: UserApi) : UserRepo {
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

    override fun getOwnUser(): Single<User> {
        return userApi.getOwnUser()
            .map { it.toUserDomain() }
    }
}
