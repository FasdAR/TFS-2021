package ru.fasdev.tfs.data.source.network.users.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class User(
    @SerialName("user_id") override val userId: Long,
    override val email: String,
    @SerialName("full_name") override val fullName: String,
    @SerialName("avatar_url") override val avatarUrl: String,
    @SerialName("is_bot") val isBot: Boolean
) : BaseUser
