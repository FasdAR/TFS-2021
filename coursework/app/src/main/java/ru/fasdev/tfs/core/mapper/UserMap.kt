package ru.fasdev.tfs.data.old.mapper

import ru.fasdev.tfs.domain.old.user.model.User
import ru.fasdev.tfs.domain.old.user.model.UserStatus
import ru.fasdev.tfs.screen.fragment.people.recycler.viewType.UserUi

fun User.toUserUi(userStatus: UserStatus) = UserUi(id.toInt(), avatarUrl, fullName, email, userStatus)
fun List<User>.mapToUserUi(checkOnline: (email: String) -> UserStatus) = map { it.toUserUi(checkOnline(it.email)) }
