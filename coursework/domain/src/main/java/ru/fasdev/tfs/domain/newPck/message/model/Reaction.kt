package ru.fasdev.tfs.domain.newPck.message.model

class Reaction(
    val emoji: String,
    val emojiName: String,
    val countSelection: Int,
    val isSelected: Boolean
)