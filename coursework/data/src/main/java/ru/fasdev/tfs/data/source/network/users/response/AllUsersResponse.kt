package ru.fasdev.tfs.data.source.network.users.response

import ru.fasdev.tfs.data.source.network.response.Result
import ru.fasdev.tfs.data.source.network.users.model.user.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.fasdev.tfs.data.source.network.response.BaseResponse

@Serializable
class AllUsersResponse(
    override val code: String? = null,
    override val msg: String,
    override val result: Result,
    @SerialName("members") val members: List<User>
): BaseResponse