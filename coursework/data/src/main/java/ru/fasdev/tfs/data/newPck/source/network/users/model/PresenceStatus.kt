package ru.fasdev.tfs.data.newPck.source.network.users.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PresenceStatus {
    @SerialName("active")
    ACTIVE,
    @SerialName("idle")
    IDLE,
    @SerialName("offline")
    OFFLINE
}