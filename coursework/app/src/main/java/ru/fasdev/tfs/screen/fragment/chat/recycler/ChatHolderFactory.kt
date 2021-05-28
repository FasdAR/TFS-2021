package ru.fasdev.tfs.screen.fragment.chat.recycler

import android.view.View
import ru.fasdev.tfs.R
import ru.fasdev.tfs.recycler.base.viewHolder.ViewHolder
import ru.fasdev.tfs.recycler.base.viewHolder.ViewHolderFactory
import ru.fasdev.tfs.recycler.item.date.DateViewHolder
import ru.fasdev.tfs.recycler.item.externalMessage.ExternalMessageViewHolder
import ru.fasdev.tfs.recycler.item.internalMessage.InternalMessageViewHolder
import ru.fasdev.tfs.recycler.item.message.MessageViewHolder

class ChatHolderFactory(
    private val reactionListener: MessageViewHolder.OnClickReactionListener,
    private val longClickListener: MessageViewHolder.OnLongClickMessageListener,
) : ViewHolderFactory() {
    override fun createViewHolder(view: View, viewType: Int): ViewHolder<*>? {
        return when (viewType) {
            R.layout.item_date_separation -> DateViewHolder(view)
            R.layout.item_external_message -> ExternalMessageViewHolder(view, reactionListener, longClickListener)
            R.layout.item_internal_message -> InternalMessageViewHolder(view, reactionListener, longClickListener)
            else -> null
        }
    }
}
