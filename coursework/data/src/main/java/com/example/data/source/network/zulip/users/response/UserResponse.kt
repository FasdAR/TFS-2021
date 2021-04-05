package com.example.data.source.network.zulip.users.response

import com.example.data.source.network.zulip.response.Result
import com.example.data.source.network.zulip.response.SharedResponse
import com.example.data.source.network.zulip.users.model.User
import com.example.data.source.network.zulip.users.model.UserContract
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UserResponse(
    override val code: String?,
    override val msg: String,
    override val result: Result,
    override val email: String,
    @SerialName("user_id") override val userId: Long,
    @SerialName("full_name") override val fullName: String,
    @SerialName("avatar_url") override val avatarUrl: String
): SharedResponse, UserContract