package ru.fasdev.tfs.data.source.network.chat.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ReactionType {
    @SerialName("unicode_emoji")
    UNICODE_EMOJI
}
