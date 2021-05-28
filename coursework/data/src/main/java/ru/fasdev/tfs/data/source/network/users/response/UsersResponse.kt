package ru.fasdev.tfs.data.source.network.users.response

import kotlinx.serialization.Serializable
import ru.fasdev.tfs.data.source.network.base.model.ZulipResult
import ru.fasdev.tfs.data.source.network.base.response.BaseResponse
import ru.fasdev.tfs.data.source.network.users.model.User

@Serializable
class UsersResponse(
    override val code: String? = null,
    override val msg: String,
    override val result: ZulipResult,
    val members: List<User>
) : BaseResponse
