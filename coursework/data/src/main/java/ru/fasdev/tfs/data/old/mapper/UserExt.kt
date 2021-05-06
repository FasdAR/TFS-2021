package ru.fasdev.tfs.data.old.mapper

import ru.fasdev.tfs.data.old.source.network.users.model.user.BaseUser

typealias DomainUser = ru.fasdev.tfs.domain.old.user.model.User

fun BaseUser.toUserDomain(): DomainUser = DomainUser(userId, avatarUrl, fullName, email)
