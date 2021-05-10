package ru.fasdev.tfs.data.source.network.events.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.fasdev.tfs.data.source.network.messages.model.Message

@Serializable
class Event(
    val type: EventType,
    val id: Int,
    val message: Message? = null,
    val op: Operation? = null,
    @SerialName("user_id") val userId: Long? = null,
    @SerialName("message_id") val messageId: Long? = null,
    @SerialName("emoji_name") val emojiName: String? = null,
    @SerialName("emoji_code") val emojiCode: String? = null,
)