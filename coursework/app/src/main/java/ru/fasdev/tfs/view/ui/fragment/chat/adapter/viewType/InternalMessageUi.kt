package ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewType

import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.ui.global.view.viewGroup.message.model.MessageReactionUi

//Здесь дата класс для генерации equals and hashcode
data class InternalMessageUi(
    override val uId: Int,
    override val message: String,
    override val reactions: List<MessageReactionUi>,
    override val viewType: Int = R.layout.item_internal_message
) : MessageUi()
