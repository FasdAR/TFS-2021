package ru.fasdev.tfs.data.source.network.zulip.stream.response

import ru.fasdev.tfs.data.source.network.zulip.stream.model.Topic
import ru.fasdev.tfs.data.source.network.zulip.response.Result
import ru.fasdev.tfs.data.source.network.zulip.response.SharedResponse
import kotlinx.serialization.Serializable

@Serializable
class TopicsResponse (
    override val code: String?,
    override val msg: String,
    override val result: Result,
    val topics: List<Topic>
) : SharedResponse