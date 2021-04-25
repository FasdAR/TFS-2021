package ru.fasdev.tfs.view.feature.mapper

import ru.fasdev.tfs.domain.model.Message
import ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewType.ExternalMessageUi
import ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewType.InternalMessageUi
import ru.fasdev.tfs.view.ui.global.recycler.base.ViewType
import java.text.SimpleDateFormat
import java.util.Locale

private const val FORMAT_DATE_YYYY_MM_DD = "yyyyMMdd"
private const val FORMAT_DATE_DD_MMM = "dd MMM"

fun Message.toExternalMessageUi() =
    ExternalMessageUi(
        id, sender.fullName, sender.avatarUrl, text,
        this.reactions.mapToMessageReactionUi()
    )

fun Message.toInternalMessageUi() =
    InternalMessageUi(id, text, this.reactions.mapToMessageReactionUi())

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
                val isInternal = it.sender.id == internalUserId
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
