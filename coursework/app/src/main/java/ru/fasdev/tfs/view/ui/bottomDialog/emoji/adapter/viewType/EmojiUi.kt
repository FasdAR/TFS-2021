package ru.fasdev.tfs.view.ui.bottomDialog.emoji.adapter.viewType

import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.ui.global.recycler.base.ViewType

class EmojiUi(
    override val uId: Int,
    val emoji: String,
    override val viewType: Int = R.layout.item_emoji
) : ViewType()
