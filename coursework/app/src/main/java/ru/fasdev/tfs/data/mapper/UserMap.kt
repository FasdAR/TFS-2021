package ru.fasdev.tfs.data.mapper

import ru.fasdev.tfs.domain.user.model.User
import ru.fasdev.tfs.recycler.item.user.UserItem

fun User.toUserItem(): UserItem {
    return UserItem(
        uId = id.toInt(),
        avatarSrc = avatarUrl,
        fullName = fullName,
        email = email,
        userStatus = onlineStatus,
    )
}