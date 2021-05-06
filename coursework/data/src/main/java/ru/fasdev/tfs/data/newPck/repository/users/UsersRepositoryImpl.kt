package ru.fasdev.tfs.data.newPck.repository.users

import io.reactivex.Maybe
import io.reactivex.Single
import ru.fasdev.tfs.data.newPck.mapper.toUser
import ru.fasdev.tfs.data.newPck.source.network.users.api.UserApi
import ru.fasdev.tfs.data.newPck.source.network.users.model.BaseUser
import ru.fasdev.tfs.domain.newPck.user.model.User

class UsersRepositoryImpl(private val userApi: UserApi) : UsersRepository
{
    override fun getOwnUser(): Single<User> {
        return userApi.getOwnUser()
            .map { (it as BaseUser).toUser() }
    }
}