package ru.fasdev.tfs.recycler.item.internalMessage

import ru.fasdev.tfs.R
import ru.fasdev.tfs.recycler.item.message.MessageItem
import ru.fasdev.tfs.view.message.base.model.MessageReactionUi

data class InternalMessageItem(
    override val uId: Int,
    override val message: String,
    override val reactions: List<MessageReactionUi>,
    override val viewType: Int = R.layout.item_internal_message
) : MessageItem()
