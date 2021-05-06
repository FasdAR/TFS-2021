package ru.fasdev.tfs.data.newPck.mapper

import ru.fasdev.tfs.data.newPck.source.network.users.model.BaseUser
import ru.fasdev.tfs.domain.newPck.user.model.User

fun BaseUser.toUser(): User {
    return User(
        id = userId,
        avatarUrl = avatarUrl,
        fullName = fullName,
        email = email
    )
}