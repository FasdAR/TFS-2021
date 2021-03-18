package ru.fasdev.tfs.view.ui.bottomDialog.emoji.adapter.viewHolder

import android.view.View
import android.widget.TextView
import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.ui.bottomDialog.emoji.adapter.viewType.EmojiUi
import ru.fasdev.tfs.view.ui.global.recycler.base.BaseViewHolder

class EmojiViewHolder(
    private val view: View,
    private val onSelectedListener: OnSelectedListener?
) :
    BaseViewHolder<EmojiUi>(view) {

    interface OnSelectedListener {
        fun onSelectedEmoji(emoji: String)
    }

    val text: TextView = view.findViewById(R.id.emoji)
    override fun bind(item: EmojiUi) {
        text.text = item.emoji
        view.setOnClickListener {
            onSelectedListener?.onSelectedEmoji(item.emoji)
        }
    }
}
