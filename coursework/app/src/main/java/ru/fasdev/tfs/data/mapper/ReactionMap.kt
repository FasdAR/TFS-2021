package ru.fasdev.tfs.data.mapper

import ru.fasdev.tfs.domain.model.Reaction
import ru.fasdev.tfs.view.message.base.model.MessageReactionUi

fun Reaction.toMessageReactionUi() = MessageReactionUi(emoji, countSelection, isSelected)

fun List<Reaction>.mapToMessageReactionUi() = map { it.toMessageReactionUi() }
