package ru.fasdev.tfs.screen.bottomDialog.selectedEmoji.adapter.viewHolder

import ru.fasdev.tfs.databinding.ItemEmojiBinding
import ru.fasdev.tfs.recycler.base.viewHolder.ViewHolder
import ru.fasdev.tfs.screen.bottomDialog.selectedEmoji.adapter.viewType.EmojiUi

class EmojiViewHolder(
    private val viewBinding: ItemEmojiBinding,
    private val onSelectedListener: OnSelectedListener?
) : ViewHolder<EmojiUi>(viewBinding.root) {

    interface OnSelectedListener {
        fun onSelectedEmoji(emoji: String)
    }

    override fun bind(item: EmojiUi) {
        viewBinding.root.text = item.emoji
        viewBinding.root.setOnClickListener {
            onSelectedListener?.onSelectedEmoji(item.emojiName)
        }
    }
}
