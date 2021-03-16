package ru.fasdev.tfs.view.ui.bottomDialog.emoji.adapter.viewHolders

import android.view.View
import android.widget.TextView
import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.feature.recycler.base.BaseViewHolder
import ru.fasdev.tfs.view.ui.bottomDialog.emoji.adapter.viewTypes.EmojiUi

class EmojiViewHolder(view: View) : BaseViewHolder<EmojiUi>(view) {
    val text: TextView = view.findViewById(R.id.emoji)
    override fun bind(item: EmojiUi) {
        text.text = item.emoji
    }
}
