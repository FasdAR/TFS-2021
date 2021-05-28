package ru.fasdev.tfs.recycler.item.externalMessage

import android.view.View
import ru.fasdev.tfs.databinding.ItemExternalMessageBinding
import ru.fasdev.tfs.recycler.item.message.MessageViewHolder

class ExternalMessageViewHolder(
    view: View,
    reactionListener: OnClickReactionListener,
    messageLongListener: OnLongClickMessageListener
) : MessageViewHolder<ExternalMessageItem>(view, reactionListener, messageLongListener) {
    private val binding = ItemExternalMessageBinding.bind(view)

    override fun bind(item: ExternalMessageItem) {
        super.bind(item)

        binding.message.avatarSrc = item.avatarSrc
        binding.message.name = item.nameSender
        binding.message.message = item.message
        binding.message.reactions = item.reactions
    }
}
