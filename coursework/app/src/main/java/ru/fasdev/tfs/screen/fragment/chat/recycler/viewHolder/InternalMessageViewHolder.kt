package ru.fasdev.tfs.screen.fragment.chat.recycler.viewHolder

import ru.fasdev.tfs.screen.fragment.chat.recycler.viewType.InternalMessageUi
import ru.fasdev.tfs.databinding.ItemInternalMessageBinding

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
