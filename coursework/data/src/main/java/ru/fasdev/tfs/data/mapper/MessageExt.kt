package ru.fasdev.tfs.data.mapper

import ru.fasdev.tfs.data.source.network.chat.model.Message
import ru.fasdev.tfs.data.source.network.chat.model.Reaction
import ru.fasdev.tfs.data.source.network.users.model.user.BaseUser

typealias DomainMessage = ru.fasdev.tfs.domain.message.model.Message
typealias DomainReaction = ru.fasdev.tfs.domain.message.model.Reaction

fun Message.mapToDomain(userId: Long): DomainMessage = DomainMessage(
    id = id,
    sender = (this as BaseUser).toUserDomain(),
    text = content,
    date = timestamp,
    reactions = reactions.mapToDomain(userId)
)

fun List<Reaction>.mapToDomain(userId: Long): List<DomainReaction> {
    val returnList: MutableMap<String, DomainReaction> = mutableMapOf()

    filter {
        !returnList.containsKey(it.emojiName)
    }.map { reaction ->
        val allReactionList = this.filter { it.emojiName == reaction.emojiName }
        val emoji = Emoji.values().findLast { it.nameInZulip == reaction.emojiName }?.unicode
        val sumReaction = allReactionList.size
        val isSelected = allReactionList.indexOfFirst { it.userId == userId } != -1

        emoji?.let {
            returnList.put(reaction.emojiName, DomainReaction(it, sumReaction, isSelected))
        }
    }

    return returnList.toList().map { it.second }
}