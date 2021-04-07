package ru.fasdev.tfs.data.mapper

import ru.fasdev.tfs.domain.model.User
import ru.fasdev.tfs.screen.fragment.people.recycler.viewType.UserUi

fun User.toUserUi(isOnline: Boolean) = UserUi(id, avatarUrl, fullName, email, isOnline)
fun List<User>.mapToUserUi(checkOnline: (idUser: Int) -> Boolean) = map { it.toUserUi(checkOnline(it.id)) }
