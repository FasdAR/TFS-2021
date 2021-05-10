package ru.fasdev.tfs.data.source.network.streams.response

import kotlinx.serialization.Serializable
import ru.fasdev.tfs.data.source.network.base.model.ZulipResult
import ru.fasdev.tfs.data.source.network.base.response.BaseResponse
import ru.fasdev.tfs.data.source.network.streams.model.Stream

@Serializable
class StreamsResponse(
    override val code: String? = null,
    override val msg: String,
    override val result: ZulipResult,
    val streams: List<Stream>
) : BaseResponse