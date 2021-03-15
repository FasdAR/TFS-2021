package ru.fasdev.tfs.view.feature.mapper

import ru.fasdev.tfs.domain.model.Reaction
import ru.fasdev.tfs.view.feature.customView.viewGroup.message.model.MessageReactionUi

fun Reaction.toMessageReactionUi() = MessageReactionUi(emoji, countSelected, isSelected)

fun List<Reaction>.mapToMessageReactionUi() = map { it.toMessageReactionUi() }