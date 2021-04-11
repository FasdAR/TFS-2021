package ru.fasdev.tfs.data.mapper

import ru.fasdev.tfs.domain.message.model.Reaction
import ru.fasdev.tfs.view.message.base.model.MessageReactionUi

fun Reaction.toMessageReactionUi() = MessageReactionUi(emoji, emojiName, countSelection, isSelected)

fun List<Reaction>.mapToMessageReactionUi() = map { it.toMessageReactionUi() }
