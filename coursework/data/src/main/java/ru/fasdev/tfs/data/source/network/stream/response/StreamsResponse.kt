package ru.fasdev.tfs.data.source.network.stream.response

import ru.fasdev.tfs.data.source.network.stream.model.Stream
import ru.fasdev.tfs.data.source.network.response.Result
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.fasdev.tfs.data.source.network.response.BaseResponse

@Serializable
class StreamsResponse (
    override val code: String?,
    override val msg: String,
    override val result: Result,
    @SerialName("streams") val streams: List<Stream>
): BaseResponse