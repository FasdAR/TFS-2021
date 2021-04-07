package ru.fasdev.tfs.data.source.network.zulip.users.response

import ru.fasdev.tfs.data.source.network.zulip.response.Result
import ru.fasdev.tfs.data.source.network.zulip.response.SharedResponse
import ru.fasdev.tfs.data.source.network.zulip.users.model.Presence
import kotlinx.serialization.Serializable

@Serializable
class UserPresenceResponse(
    override val code: String?,
    override val msg: String,
    override val result: Result,
    val presence: Presence
): SharedResponse