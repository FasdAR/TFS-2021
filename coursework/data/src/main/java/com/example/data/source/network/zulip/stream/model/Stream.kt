package com.example.data.source.network.zulip.stream.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Stream(val name: String, @SerialName("stream_id") val streamId: Long, val color: String?)