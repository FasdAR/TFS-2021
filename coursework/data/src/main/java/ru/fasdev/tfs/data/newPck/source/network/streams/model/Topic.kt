package ru.fasdev.tfs.data.newPck.source.network.streams.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Topic(
    val name: String,
    @SerialName("max_id") val maxId: Int
)