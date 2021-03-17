package ru.fasdev.tfs.view.feature.mapper

import ru.fasdev.tfs.domain.model.Reaction
import ru.fasdev.tfs.view.ui.global.view.viewGroup.message.model.MessageReactionUi

fun Reaction.toMessageReactionUi() = MessageReactionUi(emoji, countSelection, isSelected)

fun List<Reaction>.mapToMessageReactionUi() = map { it.toMessageReactionUi() }
