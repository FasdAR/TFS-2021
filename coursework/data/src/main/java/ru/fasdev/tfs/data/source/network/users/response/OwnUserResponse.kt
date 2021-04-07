package ru.fasdev.tfs.data.source.network.users.response

import ru.fasdev.tfs.data.source.network.response.Result
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.fasdev.tfs.data.source.network.response.BaseResponse
import ru.fasdev.tfs.data.source.network.users.model.user.BaseUser

@Serializable
class OwnUserResponse(
    override val code: String?,
    override val msg: String,
    override val result: Result,
    override val email: String,
    @SerialName("user_id") override val userId: Long,
    @SerialName("full_name") override val fullName: String,
    @SerialName("avatar_url") override val avatarUrl: String
): BaseResponse, BaseUser