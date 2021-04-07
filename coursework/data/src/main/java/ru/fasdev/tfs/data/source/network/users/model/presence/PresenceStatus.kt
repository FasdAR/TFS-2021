package ru.fasdev.tfs.data.source.network.users.model.presence

import kotlinx.serialization.SerialName

enum class PresenceStatus {
    @SerialName("active")
    ACTIVE,
    @SerialName("idle")
    IDLE,
    @SerialName("offline")
    OFFLINE
}