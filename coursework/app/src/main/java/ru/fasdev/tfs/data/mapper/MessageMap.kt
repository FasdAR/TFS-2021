package ru.fasdev.tfs.data.mapper

import ru.fasdev.tfs.domain.message.model.Message
import ru.fasdev.tfs.recycler.viewHolder.ViewType
import ru.fasdev.tfs.screen.fragment.chat.recycler.viewType.ExternalMessageUi
import ru.fasdev.tfs.screen.fragment.chat.recycler.viewType.InternalMessageUi
import java.text.SimpleDateFormat
import java.util.Locale

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
