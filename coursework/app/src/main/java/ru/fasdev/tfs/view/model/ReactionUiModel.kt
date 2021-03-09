package ru.fasdev.tfs.view.model

data class ReactionUiModel(val emoji: String, val reactionCount: Int, val isSelected: Boolean = false)