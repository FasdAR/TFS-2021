package ru.fasdev.tfs.data.old.mapper

import ru.fasdev.tfs.domain.old.message.model.Message
import ru.fasdev.tfs.recycler.base.viewHolder.ViewType
import ru.fasdev.tfs.recycler.item.externalMessage.ExternalMessageItem
import ru.fasdev.tfs.recycler.item.internalMessage.InternalMessageItem

fun Message.toExternalMessageUi(): ExternalMessageItem {
    return ExternalMessageItem(
        id.toInt(), sender.fullName, sender.avatarUrl, text, this.reactions.mapToMessageReactionUi()
    )
}

fun Message.toInternalMessageUi(): InternalMessageItem {
    return InternalMessageItem(id.toInt(), text, this.reactions.mapToMessageReactionUi())
}

fun List<Message>.mapToUiList(internalUserId: Long): List<ViewType> {
    return map { message ->
        if (message.sender.id == internalUserId) message.toInternalMessageUi()
        else message.toExternalMessageUi()
    }
}
