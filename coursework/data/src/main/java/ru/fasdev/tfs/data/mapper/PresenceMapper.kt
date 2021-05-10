package ru.fasdev.tfs.data.mapper

import ru.fasdev.tfs.data.source.network.users.model.Presence
import ru.fasdev.tfs.data.source.network.users.model.PresenceStatus
import ru.fasdev.tfs.domain.user.model.UserOnlineStatus

fun Presence.toUserOnlineStatus(): UserOnlineStatus {
    return when(aggregated.status) {
        PresenceStatus.ACTIVE -> UserOnlineStatus.ONLINE
        PresenceStatus.IDLE -> UserOnlineStatus.IDLE
        PresenceStatus.OFFLINE -> UserOnlineStatus.OFFLINE
    }
}