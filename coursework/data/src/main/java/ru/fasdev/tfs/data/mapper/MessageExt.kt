package ru.fasdev.tfs.data.mapper

import ru.fasdev.tfs.data.source.network.chat.model.Message
import ru.fasdev.tfs.data.source.network.chat.model.Reaction
import ru.fasdev.tfs.data.source.network.users.model.user.BaseUser

typealias DomainMessage = ru.fasdev.tfs.domain.message.model.Message
typealias DomainReaction = ru.fasdev.tfs.domain.message.model.Reaction

fun Message.mapToDomain(): DomainMessage = DomainMessage(
    id = id,
    sender = (this as BaseUser).toUserDomain(),
    text = content,
    date = timestamp,
    reactions = listOf() //TODO: ADD SET REACTIONS
)

//fun List<Reaction>.mapToDomain(): List<DomainReaction> = map {  }
