package ru.fasdev.tfs.data.mapper

typealias NetworkUser = ru.fasdev.tfs.data.source.network.users.model.user.User
typealias DomainUser = ru.fasdev.tfs.domain.user.model.User

fun NetworkUser.toUserDomain(): DomainUser = DomainUser(userId, avatarUrl, fullName, email)