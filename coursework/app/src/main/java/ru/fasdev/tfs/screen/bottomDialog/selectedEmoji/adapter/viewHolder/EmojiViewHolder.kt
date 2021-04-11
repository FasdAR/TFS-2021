package ru.fasdev.tfs.screen.bottomDialog.selectedEmoji.adapter.viewHolder

import android.view.View
import android.widget.TextView
import ru.fasdev.tfs.R
import ru.fasdev.tfs.screen.bottomDialog.selectedEmoji.adapter.viewType.EmojiUi
import ru.fasdev.tfs.recycler.viewHolder.ViewHolder

class EmojiViewHolder(
    private val view: View,
    private val onSelectedListener: OnSelectedListener?
) : ViewHolder<EmojiUi>(view) {

    interface OnSelectedListener {
        fun onSelectedEmoji(emoji: String)
    }

    val text: TextView = view.findViewById(R.id.emoji)
    override fun bind(item: EmojiUi) {
        text.text = item.emoji
        view.setOnClickListener {
            onSelectedListener?.onSelectedEmoji(item.emojiName)
        }
    }
}
