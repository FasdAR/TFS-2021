package ru.fasdev.tfs.domain.model

import java.util.*

class Message (val id: Int, val sender: User, val text: String, val date: Date, val reactions: MutableList<Reaction>)