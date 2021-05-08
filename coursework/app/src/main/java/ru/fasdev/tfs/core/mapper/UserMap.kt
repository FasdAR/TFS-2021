package ru.fasdev.tfs.data.old.mapper

import ru.fasdev.tfs.domain.old.user.model.User
import ru.fasdev.tfs.domain.old.user.model.UserStatus
import ru.fasdev.tfs.recycler.item.user.UserItem

fun User.toUserUi(userStatus: UserStatus) = UserItem(id.toInt(), avatarUrl, fullName, email, userStatus)
fun List<User>.mapToUserUi(checkOnline: (email: String) -> UserStatus) = map { it.toUserUi(checkOnline(it.email)) }
