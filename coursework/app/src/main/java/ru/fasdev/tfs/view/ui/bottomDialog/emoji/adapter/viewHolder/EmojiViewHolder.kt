package ru.fasdev.tfs.view.ui.bottomDialog.emoji.adapter.viewHolder

import ru.fasdev.tfs.databinding.ItemEmojiBinding
import ru.fasdev.tfs.view.ui.bottomDialog.emoji.adapter.viewType.EmojiUi
import ru.fasdev.tfs.view.ui.global.recycler.base.BaseViewHolder

class EmojiViewHolder(
    private val viewBinding: ItemEmojiBinding,
    private val onSelectedListener: OnSelectedListener?
) : BaseViewHolder<EmojiUi>(viewBinding.root) {
    interface OnSelectedListener {
        fun onSelectedEmoji(emoji: String)
    }

    override fun bind(item: EmojiUi) {
        viewBinding.root.text = item.emoji
        viewBinding.root.setOnClickListener {
            onSelectedListener?.onSelectedEmoji(item.emoji)
        }
    }
}
