package ru.fasdev.tfs.recycler.item.externalMessage

import ru.fasdev.tfs.R
import ru.fasdev.tfs.recycler.item.message.MessageItem
import ru.fasdev.tfs.view.message.base.model.MessageReactionUi

data class ExternalMessageItem(
    override val uId: Int,
    val nameSender: String,
    val avatarSrc: String,
    override val message: String,
    override val reactions: List<MessageReactionUi>,
    override val viewType: Int = R.layout.item_external_message
) : MessageItem()
