package ru.fasdev.tfs.recycler.item.internalMessage

import android.view.View
import ru.fasdev.tfs.databinding.ItemInternalMessageBinding
import ru.fasdev.tfs.recycler.item.message.MessageViewHolder

class InternalMessageViewHolder(
    view: View,
    reactionListener: OnClickReactionListener,
    messageLongListener: OnLongClickMessageListener
) : MessageViewHolder<InternalMessageItem>(view, reactionListener, messageLongListener) {
    private val binding = ItemInternalMessageBinding.bind(view)

    override fun bind(item: InternalMessageItem) {
        super.bind(item)

        binding.message.message = item.message
        binding.message.reactions = item.reactions
    }
}
