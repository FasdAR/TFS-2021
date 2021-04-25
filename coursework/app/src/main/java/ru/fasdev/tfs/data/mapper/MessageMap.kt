package ru.fasdev.tfs.data.mapper

import ru.fasdev.tfs.domain.message.model.Message
import ru.fasdev.tfs.recycler.viewHolder.ViewType
import ru.fasdev.tfs.screen.fragment.chat.recycler.viewType.ExternalMessageUi
import ru.fasdev.tfs.screen.fragment.chat.recycler.viewType.InternalMessageUi
import java.text.SimpleDateFormat
import java.util.Locale

private const val FORMAT_DATE_YYYY_MM_DD = "yyyyMMdd"
private const val FORMAT_DATE_DD_MMM = "dd MMM"

fun Message.toExternalMessageUi(): ExternalMessageUi {
    return ExternalMessageUi(
        id.toInt(), sender.fullName, sender.avatarUrl, text, this.reactions.mapToMessageReactionUi()
    )
}

fun Message.toInternalMessageUi(): InternalMessageUi {
    return InternalMessageUi(id.toInt(), text, this.reactions.mapToMessageReactionUi())
}

fun List<Message>.mapToUiList(internalUserId: Int): List<ViewType> {
    val dateFormatKey = SimpleDateFormat(FORMAT_DATE_YYYY_MM_DD, Locale.getDefault())
    val dateFormatUi = SimpleDateFormat(FORMAT_DATE_DD_MMM, Locale.getDefault())

    val mapMsgDate = groupBy { dateFormatKey.format(it.date) }

    return mapMsgDate
        .keys
        .sorted()
        .flatMap { key ->
            val date = dateFormatKey.parse(key)
            val items = mapMsgDate[key]?.map {
                val isInternal = it.sender.id.toInt() == internalUserId
                if (isInternal) {
                    it.toInternalMessageUi()
                } else {
                    it.toExternalMessageUi()
                }
            } ?: emptyList()

            val list = date?.let { listOf(date.toDateUi(dateFormatUi)) } ?: emptyList()
            list + items
        }
        .reversed()
}
