package ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewHolder

import android.view.View
import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewType.InternalMessageUi
import ru.fasdev.tfs.view.ui.global.view.viewGroup.message.InternalMessageViewGroup

class InternalMessageViewHolder(
    view: View,
    private val reactionListener: OnClickReactionListener,
    private val messageLongListener: OnLongClickMessageListener
) : MessageViewHolder<InternalMessageUi>(view, reactionListener, messageLongListener) {
    private val message: InternalMessageViewGroup = view.findViewById(R.id.message)

    override fun bind(item: InternalMessageUi) {
        super.bind(item)

        message.message = item.message
        message.reactions = item.reactions
    }
}
