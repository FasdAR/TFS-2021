package ru.fasdev.tfs.data.mapper

import ru.fasdev.tfs.data.source.network.messages.model.Message
import ru.fasdev.tfs.data.source.network.users.model.BaseUser

typealias MessageDomain = ru.fasdev.tfs.domain.message.model.Message

fun Message.toMessageDomain(userId: Long): MessageDomain {
    return MessageDomain(
        id = id,
        sender = (this as BaseUser).toUser(),
        text = content,
        date = timestamp,
        reactions = reactions.toDomainReaction(userId)
    )
}