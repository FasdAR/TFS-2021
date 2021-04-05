package com.example.data.source.network.zulip.users.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface UserContract {
    val email: String
    val userId: Long
    val fullName: String
    val avatarUrl: String
}

@Serializable
class User(
    override val email: String,
    @SerialName("user_id") override val userId: Long,
    @SerialName("full_name") override val fullName: String,
    @SerialName("avatar_url") override val avatarUrl: String
) : UserContract