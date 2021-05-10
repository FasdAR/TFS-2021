package ru.fasdev.tfs.data.mapper

import ru.fasdev.tfs.data.source.network.emoji.EmojiList
import ru.fasdev.tfs.data.source.network.messages.model.Reaction

typealias ReactionDomain = ru.fasdev.tfs.domain.message.model.Reaction
fun List<Reaction>.toDomainReaction(userId: Long): List<ReactionDomain> {
    return groupBy { it.emojiCode }
        .map { mapReactions ->
            val item = mapReactions.value.first()
            ReactionDomain(
                emoji = EmojiList.values().findLast { it.nameInZulip == item.emojiName }?.unicode.toString(),
                emojiName = item.emojiName,
                countSelection = mapReactions.value.size,
                isSelected = mapReactions.value.filter { it.userId == userId }.isNotEmpty()
            )
        }
}