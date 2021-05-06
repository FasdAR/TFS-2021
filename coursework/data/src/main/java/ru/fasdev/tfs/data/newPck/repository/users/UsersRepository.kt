package ru.fasdev.tfs.data.newPck.repository.users

import io.reactivex.Single
import ru.fasdev.tfs.domain.newPck.user.model.User

interface UsersRepository
{
    fun getOwnUser(): Single<User>
}