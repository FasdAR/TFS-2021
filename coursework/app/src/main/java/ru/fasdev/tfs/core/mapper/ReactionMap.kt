package ru.fasdev.tfs.data.old.mapper

import ru.fasdev.tfs.domain.old.message.model.Reaction
import ru.fasdev.tfs.view.message.base.model.MessageReactionUi

fun Reaction.toMessageReactionUi() = MessageReactionUi(emoji, emojiName, countSelection, isSelected)

fun List<Reaction>.mapToMessageReactionUi() = map { it.toMessageReactionUi() }
