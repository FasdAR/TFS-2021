package ru.fasdev.tfs.data.mapper

import ru.fasdev.tfs.domain.message.model.Message
import ru.fasdev.tfs.screen.fragment.chat.recycler.viewType.ExternalMessageUi
import ru.fasdev.tfs.screen.fragment.chat.recycler.viewType.InternalMessageUi
import ru.fasdev.tfs.recycler.viewHolder.ViewType
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

fun List<Message>.mapToUiList(internalUserId: Int): List<ViewType> {
    val dateFormatKey = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    val dateFormatUi = SimpleDateFormat("dd MMM", Locale.getDefault())

    val mapMsgDate = groupBy { dateFormatKey.format(it.date) }
    val resultList: MutableList<ViewType> = mutableListOf()

    mapMsgDate
        .keys
        .sorted()
        .forEach { key ->
            val date = dateFormatKey.parse(key)
            val items = mapMsgDate[key]

            date?.let { date ->
                resultList.add(date.toDateUi(dateFormatUi))
            }

            items?.forEach { message ->
                if (message.sender.id.toInt() == internalUserId) resultList.add(message.toInternalMessageUi())
                else resultList.add(message.toExternalMessageUi())
            }
        }

    return resultList.reversed()
}
