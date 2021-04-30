package ru.fasdev.tfs.screen.fragment.chat.recycler.viewType

import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.message.base.model.MessageReactionUi

//Здесь дата класс для генерации equals and hashcode
data class ExternalMessageUi(
    override val uId: Int,
    val nameSender: String,
    val avatarSrc: String,
    override val message: String,
    override val reactions: List<MessageReactionUi>,
    override val viewType: Int = R.layout.item_external_message
) : MessageUi()
