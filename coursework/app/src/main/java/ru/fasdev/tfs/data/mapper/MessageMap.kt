package ru.fasdev.tfs.data.mapper

import ru.fasdev.tfs.domain.message.model.Message
import ru.fasdev.tfs.recycler.item.externalMessage.ExternalMessageItem
import ru.fasdev.tfs.recycler.item.internalMessage.InternalMessageItem
import ru.fasdev.tfs.recycler.item.message.MessageItem

fun Message.toMessageItem(isOwnMessage: Boolean): MessageItem {
    if (isOwnMessage) {
        return InternalMessageItem(
            uId = id.toInt(),
            message = text,
            reactions = reactions.map { it.toReactionItem() }
        )
    } else {
        return ExternalMessageItem(
            uId = id.toInt(),
            nameSender = sender.fullName,
            avatarSrc = sender.avatarUrl,
            message = text,
            reactions = reactions.map { it.toReactionItem() }
        )
    }
}
