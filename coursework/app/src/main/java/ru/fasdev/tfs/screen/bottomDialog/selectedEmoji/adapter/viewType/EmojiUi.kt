package ru.fasdev.tfs.screen.bottomDialog.selectedEmoji.adapter.viewType

import ru.fasdev.tfs.R
import ru.fasdev.tfs.core.ext.toDp
import ru.fasdev.tfs.recycler.base.viewHolder.ViewType

class EmojiUi(
    override val uId: Int,
    val emoji: String,
    val emojiName: String,
    override val viewType: Int = R.layout.item_emoji
) : ViewType() {
    companion object {
        val COLUMN_WIDTH = 55.toDp
    }
}
