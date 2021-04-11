package ru.fasdev.tfs.data.source.network.chat.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Reaction(
    @SerialName("emoji_name") val emojiName: String,
    @SerialName("emoji_code") val emojiCode: String,
    @SerialName("user_id") val userId: Long
)
