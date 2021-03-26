package ru.fasdev.tfs.view.feature.mapper

import ru.fasdev.tfs.domain.model.User
import ru.fasdev.tfs.view.ui.fragment.people.adapter.viewType.UserUi

fun User.toUserUi(isOnline: Boolean) = UserUi(id, avatarUrl, fullName, email, isOnline)
fun List<User>.mapToUserUi(checkOnline: (idUser: Int) -> Boolean) = map { it.toUserUi(checkOnline(it.id)) }
