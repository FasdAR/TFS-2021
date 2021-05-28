package ru.fasdev.tfs.data.source.network.events.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.fasdev.tfs.data.source.network.base.model.ZulipResult
import ru.fasdev.tfs.data.source.network.base.response.BaseResponse

@Serializable
class RegisterResponse(
    override val code: String? = null,
    override val msg: String,
    override val result: ZulipResult,
    @SerialName("queue_id") val queueId: String,
    @SerialName("last_event_id") val lastEventId: Int
) : BaseResponse
