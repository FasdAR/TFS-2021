package ru.fasdev.tfs.data.newPck.source.network.events.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.fasdev.tfs.data.newPck.source.network.base.model.ZulipResult
import ru.fasdev.tfs.data.newPck.source.network.base.response.BaseResponse
import ru.fasdev.tfs.data.newPck.source.network.events.model.Event

@Serializable
class EventsResponse (
    override val code: String? = null,
    override val msg: String,
    override val result: ZulipResult,
    val events: List<Event>,
    @SerialName("queue_id") val queueId: String
) : BaseResponse