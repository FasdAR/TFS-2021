package ru.fasdev.tfs.data.source.network.users.model.user

import kotlinx.serialization.Serializable
import ru.fasdev.tfs.data.core.serializer.DateLongSerializer
import ru.fasdev.tfs.data.source.network.users.model.presence.PresenceStatus
import java.util.Date

@Serializable
class UserPresence(
    val status: PresenceStatus,
    @Serializable(with = DateLongSerializer::class) val timestamp: Date
)
