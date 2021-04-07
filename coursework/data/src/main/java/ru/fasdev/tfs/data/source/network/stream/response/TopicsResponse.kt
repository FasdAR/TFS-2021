package ru.fasdev.tfs.data.source.network.stream.response

import ru.fasdev.tfs.data.source.network.stream.model.Topic
import ru.fasdev.tfs.data.source.network.response.Result
import kotlinx.serialization.Serializable
import ru.fasdev.tfs.data.source.network.response.BaseResponse

@Serializable
class TopicsResponse (
    override val code: String?,
    override val msg: String,
    override val result: Result,
    val topics: List<Topic>
) : BaseResponse