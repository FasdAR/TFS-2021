package ru.fasdev.tfs.data.source.network.events.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class EventType
{
    @SerialName("message") MESSAGE,
    @SerialName("reaction") REACTION
}