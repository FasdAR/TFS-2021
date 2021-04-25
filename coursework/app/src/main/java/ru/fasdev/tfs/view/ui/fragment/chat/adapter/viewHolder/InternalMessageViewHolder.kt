package ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewHolder

import ru.fasdev.tfs.databinding.ItemInternalMessageBinding
import ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewType.InternalMessageUi

class InternalMessageViewHolder(
    private val viewBinding: ItemInternalMessageBinding,
    reactionListener: OnClickReactionListener,
    messageLongListener: OnLongClickMessageListener
) : MessageViewHolder<InternalMessageUi>(viewBinding.root, reactionListener, messageLongListener) {

    override fun bind(item: InternalMessageUi) {
        super.bind(item)

        viewBinding.message.message = item.message
        viewBinding.message.reactions = item.reactions
    }
}
