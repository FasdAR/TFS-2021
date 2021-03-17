package ru.fasdev.tfs.view.ui.bottomDialog.emoji.adapter

import android.view.View
import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.ui.global.recycler.base.BaseViewHolder
import ru.fasdev.tfs.view.ui.global.recycler.base.HolderFactory
import ru.fasdev.tfs.view.ui.bottomDialog.emoji.adapter.viewHolder.EmojiViewHolder

class EmojiHolderFactory(
        private val onSelectedEmojiListener: EmojiViewHolder.OnSelectedListener
    ) : HolderFactory() {
    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>? {
        return when (viewType) {
            R.layout.item_emoji -> EmojiViewHolder(view, onSelectedEmojiListener)
            else -> null
        }
    }
}
