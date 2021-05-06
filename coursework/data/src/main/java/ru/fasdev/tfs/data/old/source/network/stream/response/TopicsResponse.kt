package ru.fasdev.tfs.data.old.source.network.stream.response

import kotlinx.serialization.Serializable
import ru.fasdev.tfs.data.old.source.network.base.response.BaseResponse
import ru.fasdev.tfs.data.old.source.network.base.response.Result
import ru.fasdev.tfs.data.old.source.network.stream.model.Topic

@Serializable
class TopicsResponse(
    override val code: String? = null,
    override val msg: String,
    override val result: Result,
    val topics: List<Topic>
) : BaseResponse
