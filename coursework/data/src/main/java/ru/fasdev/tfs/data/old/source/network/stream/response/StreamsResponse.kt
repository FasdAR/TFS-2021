package ru.fasdev.tfs.data.old.source.network.stream.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.fasdev.tfs.data.old.source.network.base.response.BaseResponse
import ru.fasdev.tfs.data.old.source.network.base.response.Result
import ru.fasdev.tfs.data.old.source.network.stream.model.Stream

@Serializable
class StreamsResponse(
    override val code: String? = null,
    override val msg: String,
    override val result: Result,
    @SerialName("streams") val streams: List<Stream>
) : BaseResponse
