package ru.fasdev.tfs.data.old.mapper

import ru.fasdev.tfs.domain.old.message.model.Message
import ru.fasdev.tfs.recycler.viewHolder.ViewType
import ru.fasdev.tfs.screen.fragment.chat.recycler.viewType.ExternalMessageUi
import ru.fasdev.tfs.screen.fragment.chat.recycler.viewType.InternalMessageUi

fun Message.toExternalMessageUi(): ExternalMessageUi {
    return ExternalMessageUi(
        id.toInt(), sender.fullName, sender.avatarUrl, text, this.reactions.mapToMessageReactionUi()
    )
}

fun Message.toInternalMessageUi(): InternalMessageUi {
    return InternalMessageUi(id.toInt(), text, this.reactions.mapToMessageReactionUi())
}

fun List<Message>.mapToUiList(internalUserId: Long): List<ViewType> {
    return map { message ->
        if (message.sender.id == internalUserId) message.toInternalMessageUi()
        else message.toExternalMessageUi()
    }
}
