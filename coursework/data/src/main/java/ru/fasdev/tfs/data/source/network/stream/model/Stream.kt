package ru.fasdev.tfs.data.source.network.stream.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Stream(val name: String, @SerialName("stream_id") val streamId: Long, val color: String?)