package ru.fasdev.tfs.screen.bottomDialog.selectedEmoji.adapter

import android.view.View
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.ItemEmojiBinding
import ru.fasdev.tfs.recycler.viewHolder.ViewHolder
import ru.fasdev.tfs.recycler.viewHolder.ViewHolderFactory
import ru.fasdev.tfs.screen.bottomDialog.selectedEmoji.adapter.viewHolder.EmojiViewHolder

class EmojiHolderFactory(
    private val onSelectedEmojiListener: EmojiViewHolder.OnSelectedListener
) : ViewHolderFactory() {
    override fun createViewHolder(view: View, viewType: Int): ViewHolder<*>? {
        return when (viewType) {
            R.layout.item_emoji -> EmojiViewHolder(ItemEmojiBinding.bind(view), onSelectedEmojiListener)
            else -> null
        }
    }
}
