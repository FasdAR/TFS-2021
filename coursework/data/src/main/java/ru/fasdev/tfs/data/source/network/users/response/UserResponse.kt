package ru.fasdev.tfs.data.source.network.users.response

import ru.fasdev.tfs.data.source.network.response.BaseResponse
import ru.fasdev.tfs.data.source.network.response.Result
import ru.fasdev.tfs.data.source.network.users.model.user.User

class UserResponse(
    override val code: String?,
    override val msg: String,
    override val result: Result,
    val user: User
): BaseResponse