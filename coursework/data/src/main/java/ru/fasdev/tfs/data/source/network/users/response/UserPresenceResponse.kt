package ru.fasdev.tfs.data.source.network.users.response

import kotlinx.serialization.Serializable
import ru.fasdev.tfs.data.source.network.base.response.BaseResponse
import ru.fasdev.tfs.data.source.network.base.response.Result
import ru.fasdev.tfs.data.source.network.users.model.presence.Presence

@Serializable
class UserPresenceResponse(
    override val code: String? = null,
    override val msg: String,
    override val result: Result,
    val presence: Presence
) : BaseResponse
