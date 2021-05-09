package ru.fasdev.tfs.data.newPck.source.network.streams.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Stream(
    val name: String,
    @SerialName("stream_id") val streamId: Long,
    val color: String? = null
)