package com.example.data.source.network.zulip.users.response

import com.example.data.source.network.zulip.response.Result
import com.example.data.source.network.zulip.response.SharedResponse
import com.example.data.source.network.zulip.users.model.Presence
import com.example.data.source.network.zulip.users.model.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UserPresenceResponse(
    override val code: String?,
    override val msg: String,
    override val result: Result,
    val presence: Presence
): SharedResponse