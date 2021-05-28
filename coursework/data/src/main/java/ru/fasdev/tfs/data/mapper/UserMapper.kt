package ru.fasdev.tfs.data.mapper

import ru.fasdev.tfs.data.source.database.model.UserDb
import ru.fasdev.tfs.data.source.network.users.model.BaseUser
import ru.fasdev.tfs.domain.user.model.User

fun BaseUser.toUser(): User {
    return User(
        id = userId,
        avatarUrl = avatarUrl,
        fullName = fullName,
        email = email
    )
}

fun User.toUserDb(): UserDb {
    return UserDb(
        id, avatarUrl, fullName, email
    )
}

fun UserDb.toDomainUser(): User {
    return User(
        id, avatarUrl, fullName, email
    )
}
