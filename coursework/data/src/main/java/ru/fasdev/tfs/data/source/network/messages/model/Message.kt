package ru.fasdev.tfs.data.source.network.messages.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.fasdev.tfs.data.source.network.base.serializer.DateLongSerializer
import ru.fasdev.tfs.data.source.network.users.model.BaseUser
import java.util.Date

@Serializable
class Message(
    val id: Long,
    @SerialName("sender_id") override val userId: Long,
    @Serializable(with = DateLongSerializer::class) val timestamp: Date,
    @SerialName("sender_full_name") override val fullName: String,
    @SerialName("sender_email") override val email: String,
    @SerialName("avatar_url") override val avatarUrl: String,
    @SerialName("stream_id") val streamId: Long,
    val subject: String,
    val reactions: List<Reaction>,
    val content: String
) : BaseUser
