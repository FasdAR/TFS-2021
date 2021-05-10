package ru.fasdev.tfs.data.newPck.source.network.events.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.fasdev.tfs.data.newPck.source.network.messages.model.Message
import ru.fasdev.tfs.data.newPck.source.network.users.model.User

@Serializable
class Event(
    val type: EventType,
    val id: Int,
    val message: Message,
    val op: Operation,
    @SerialName("user_id") val userId: Long,
    val user: User,
    @SerialName("message_id") val messageId: Long,
    @SerialName("emoji_name") val emojiName: String,
    @SerialName("emoji_code") val emojiCode: String,
)