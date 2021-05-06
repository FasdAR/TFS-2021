package ru.fasdev.tfs.data.newPck.source.network.users.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.fasdev.tfs.data.newPck.source.network.base.model.ZulipResult
import ru.fasdev.tfs.data.newPck.source.network.base.response.BaseResponse
import ru.fasdev.tfs.data.newPck.source.network.users.model.BaseUser

@Serializable
class OwnUserResponse(
    override val code: String? = null,
    override val msg: String,
    override val result: ZulipResult,
    override val email: String,
    @SerialName("user_id") override val userId: Long,
    @SerialName("full_name") override val fullName: String,
    @SerialName("avatar_url") override val avatarUrl: String
) : BaseUser, BaseResponse