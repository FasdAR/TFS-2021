package com.example.data.source.network.zulip.stream.response

import com.example.data.source.network.zulip.stream.model.Stream
import com.example.data.source.network.zulip.response.Result
import com.example.data.source.network.zulip.response.SharedResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SubStreamsResponse (
    override val code: String?,
    override val msg: String,
    override val result: Result,
    @SerialName("subscriptions") val subscriptions: List<Stream>
): SharedResponse