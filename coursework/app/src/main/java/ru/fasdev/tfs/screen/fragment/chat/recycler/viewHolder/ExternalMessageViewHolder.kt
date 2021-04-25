package ru.fasdev.tfs.screen.fragment.chat.recycler.viewHolder

import ru.fasdev.tfs.screen.fragment.chat.recycler.viewType.ExternalMessageUi
import ru.fasdev.tfs.databinding.ItemExternalMessageBinding

class ExternalMessageViewHolder(
    private val viewBinding: ItemExternalMessageBinding,
    reactionListener: OnClickReactionListener,
    messageLongListener: OnLongClickMessageListener
) : MessageViewHolder<ExternalMessageUi>(viewBinding.root, reactionListener, messageLongListener) {
    override fun bind(item: ExternalMessageUi) {
        super.bind(item)

        viewBinding.message.avatarSrc = item.avatarSrc
        viewBinding.message.name = item.nameSender
        viewBinding.message.message = item.message
        viewBinding.message.reactions = item.reactions
    }
}
