package ru.fasdev.tfs.data.source.network.messages.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Reaction(
    @SerialName("user_id") val userId: Long,
    @SerialName("emoji_name") val emojiName: String,
    @SerialName("emoji_code") val emojiCode: String
)