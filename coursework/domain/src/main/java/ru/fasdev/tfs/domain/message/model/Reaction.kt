package ru.fasdev.tfs.domain.message.model

data class Reaction(val emoji: String, val emojiName: String, val countSelection: Int, val isSelected: Boolean)
