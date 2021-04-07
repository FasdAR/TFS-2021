package ru.fasdev.tfs.screen.fragment.chat.recycler.viewType

import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.message.base.model.MessageReactionUi

data class InternalMessageUi(
    override val uId: Int,
    override val message: String,
    override val reactions: List<MessageReactionUi>,
    override val viewType: Int = R.layout.item_internal_message
) : MessageUi()
