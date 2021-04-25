package ru.fasdev.tfs.screen.fragment.chat.recycler

import android.view.View
import ru.fasdev.tfs.R
import ru.fasdev.tfs.recycler.viewHolder.ViewHolder
import ru.fasdev.tfs.recycler.viewHolder.ViewHolderFactory
import ru.fasdev.tfs.screen.fragment.chat.recycler.viewHolder.DateViewHolder
import ru.fasdev.tfs.screen.fragment.chat.recycler.viewHolder.ExternalMessageViewHolder
import ru.fasdev.tfs.screen.fragment.chat.recycler.viewHolder.InternalMessageViewHolder
import ru.fasdev.tfs.screen.fragment.chat.recycler.viewHolder.MessageViewHolder
import ru.fasdev.tfs.databinding.ItemDateSeparationBinding
import ru.fasdev.tfs.databinding.ItemExternalMessageBinding
import ru.fasdev.tfs.databinding.ItemInternalMessageBinding

class ChatHolderFactory(
    private val reactionListener: MessageViewHolder.OnClickReactionListener,
    private val longClickListener: MessageViewHolder.OnLongClickMessageListener,
) : ViewHolderFactory() {
    override fun createViewHolder(view: View, viewType: Int): ViewHolder<*>? {
        return when (viewType) {
            R.layout.item_date_separation -> DateViewHolder(ItemDateSeparationBinding.bind(view))
            R.layout.item_external_message -> ExternalMessageViewHolder(ItemExternalMessageBinding.bind(view), reactionListener, longClickListener)
            R.layout.item_internal_message -> InternalMessageViewHolder(ItemInternalMessageBinding.bind(view), reactionListener, longClickListener)
            else -> null
        }
    }
}
