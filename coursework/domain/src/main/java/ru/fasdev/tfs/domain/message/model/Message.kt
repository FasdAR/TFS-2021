package ru.fasdev.tfs.domain.message.model

import ru.fasdev.tfs.domain.user.model.User
import java.util.Date

class Message(
    val id: Long,
    val sender: User,
    val text: String,
    val date: Date,
    val reactions: List<Reaction>,
    val topic: String,
    val streamId: Long
)
