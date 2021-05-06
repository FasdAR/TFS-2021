package ru.fasdev.tfs.domain.old.message.model

import ru.fasdev.tfs.domain.old.user.model.User
import java.util.Date

data class Message(
    val id: Long,
    val sender: User,
    val text: String,
    val date: Date = Date(),
    val reactions: List<Reaction> = listOf()
)
