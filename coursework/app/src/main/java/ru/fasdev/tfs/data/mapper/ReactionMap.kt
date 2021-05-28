package ru.fasdev.tfs.data.mapper

import ru.fasdev.tfs.domain.message.model.Reaction
import ru.fasdev.tfs.view.message.base.model.MessageReactionUi

fun Reaction.toReactionItem(): MessageReactionUi {
    return MessageReactionUi(
        emoji = emoji,
        emojiName = emojiName,
        reactionCount = countSelection,
        isSelected = isSelected
    )
}
