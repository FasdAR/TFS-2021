package ru.fasdev.tfs.domain.message.model

import ru.fasdev.tfs.domain.user.model.User
import java.util.Date

data class Message(
    val id: Long,
    val sender: User,
    val text: String,
    val date: Date = Date(),
    val reactions: List<Reaction> = listOf()
)
