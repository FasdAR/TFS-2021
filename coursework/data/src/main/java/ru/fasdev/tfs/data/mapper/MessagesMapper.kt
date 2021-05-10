package ru.fasdev.tfs.data.mapper

import ru.fasdev.tfs.data.source.database.model.MessageDb
import ru.fasdev.tfs.data.source.database.relation.MessageRelation
import ru.fasdev.tfs.data.source.network.messages.model.Message
import ru.fasdev.tfs.data.source.network.users.model.BaseUser

typealias MessageDomain = ru.fasdev.tfs.domain.message.model.Message

fun Message.toMessageDomain(userId: Long): MessageDomain {
    return MessageDomain(
        id = id,
        sender = (this as BaseUser).toUser(),
        text = content,
        date = timestamp,
        reactions = reactions.toDomainReaction(userId),
        topic = subject,
        streamId = streamId
    )
}

fun MessageDomain.toMessageDb(): MessageDb {
    return MessageDb(
        id,
        sender.id,
        topic,
        streamId,
        text,
        date
    )
}

fun MessageRelation.toMessageDomain(): MessageDomain {
    return MessageDomain(
        id = messageDb.id,
        sender = sender.toDomainUser(),
        text = messageDb.text,
        date = messageDb.date,
        reactions = reactions.map { it.toReactionDomain() },
        topic = messageDb.topic,
        streamId = messageDb.idStream
    )
}
