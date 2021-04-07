package ru.fasdev.tfs.data.source.network.zulip.users.response

import ru.fasdev.tfs.data.source.network.zulip.response.Result
import ru.fasdev.tfs.data.source.network.zulip.response.SharedResponse
import ru.fasdev.tfs.data.source.network.zulip.users.model.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UsersResponse(
    override val code: String?,
    override val msg: String,
    override val result: Result,
    @SerialName("members") val members: List<User>
): SharedResponse