package com.example.data.source.network.zulip.users.model

import com.example.data.serializer.DateAsLongSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
class UserPresence (
    val status: PresenceStatus,
    @Serializable(with = DateAsLongSerializer::class) val timestamp: Date)

enum class PresenceStatus {
    @SerialName("active") ACTIVE,
    @SerialName("idle") IDLE,
    @SerialName("offline") OFFLINE
}