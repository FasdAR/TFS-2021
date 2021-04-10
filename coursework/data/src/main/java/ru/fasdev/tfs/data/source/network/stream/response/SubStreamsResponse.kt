package ru.fasdev.tfs.data.source.network.stream.response

import ru.fasdev.tfs.data.source.network.stream.model.Stream
import ru.fasdev.tfs.data.source.network.base.response.Result
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.fasdev.tfs.data.source.network.base.response.BaseResponse

@Serializable
class SubStreamsResponse (
    override val code: String? = null,
    override val msg: String,
    override val result: Result,
    @SerialName("subscriptions") val subscriptions: List<Stream>
): BaseResponse