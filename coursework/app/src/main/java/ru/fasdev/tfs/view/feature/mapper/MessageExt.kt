package ru.fasdev.tfs.view.feature.mapper

import ru.fasdev.tfs.domain.model.Message
import ru.fasdev.tfs.view.feature.recycler.base.ViewTyped
import ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewTypes.ExternalMessageUi
import ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewTypes.InternalMessageUi
import java.text.SimpleDateFormat
import java.util.*

fun Message.toExternalMessageUi() =
    ExternalMessageUi(id, sender.fullName, sender.avatarUrl, text,
        this.reactions.mapToMessageReactionUi())

fun Message.toInternalMessageUi() =
    InternalMessageUi(id, text, this.reactions.mapToMessageReactionUi())

fun List<Message>.mapToUiList(internalUserId: Int): List<ViewTyped> {
    val dateFormatKey = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    val dateFormatUi = SimpleDateFormat("dd MMM", Locale.getDefault())

    val mapMsgDate = groupBy { dateFormatKey.format(it.date) }
    val resultList: MutableList<ViewTyped> = mutableListOf()

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
                if (message.sender.id == internalUserId) resultList.add(message.toInternalMessageUi())
                else resultList.add(message.toExternalMessageUi())
            }
        }

    return resultList.reversed()
}