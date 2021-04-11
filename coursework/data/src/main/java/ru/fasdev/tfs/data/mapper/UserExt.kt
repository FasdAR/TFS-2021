package ru.fasdev.tfs.data.mapper

import ru.fasdev.tfs.data.source.network.users.model.user.BaseUser

typealias DomainUser = ru.fasdev.tfs.domain.user.model.User

fun BaseUser.toUserDomain(): DomainUser = DomainUser(userId, avatarUrl, fullName, email)
