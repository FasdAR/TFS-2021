package ru.fasdev.tfs.view.ui.fragment.chat.adapter

import android.view.View
import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewHolder.DateViewHolder
import ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewHolder.ExternalMessageViewHolder
import ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewHolder.InternalMessageViewHolder
import ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewHolder.MessageViewHolder
import ru.fasdev.tfs.view.ui.global.recycler.base.BaseViewHolder
import ru.fasdev.tfs.view.ui.global.recycler.base.HolderFactory

class ChatHolderFactory(
    private val reactionListener: MessageViewHolder.OnClickReactionListener,
    private val longClickListener: MessageViewHolder.OnLongClickMessageListener,
) : HolderFactory() {
    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>? {
        return when (viewType) {
            R.layout.item_date_separation -> DateViewHolder(view)
            R.layout.item_external_message -> ExternalMessageViewHolder(view, reactionListener, longClickListener)
            R.layout.item_internal_message -> InternalMessageViewHolder(view, reactionListener, longClickListener)
            else -> null
        }
    }
}
