package ru.fasdev.tfs.view.feature.mapper

import ru.fasdev.tfs.domain.model.User
import ru.fasdev.tfs.view.ui.fragment.people.adapter.viewType.UserUi

fun User.toUserUi() = UserUi(id, avatarUrl, fullName, email)
fun List<User>.mapToUserUi() = map { it.toUserUi() }