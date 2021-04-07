package ru.fasdev.tfs.data.mapper

import ru.fasdev.tfs.data.source.network.users.model.presence.PresenceStatus
import ru.fasdev.tfs.data.source.network.users.model.user.UserPresence
import ru.fasdev.tfs.domain.user.model.UserStatus

fun UserPresence.toUserStatus(): UserStatus {
    return when (status) {
        PresenceStatus.ACTIVE -> UserStatus.ONLINE
        PresenceStatus.IDLE -> UserStatus.IDLE
        PresenceStatus.OFFLINE -> UserStatus.OFFLINE
    }
}