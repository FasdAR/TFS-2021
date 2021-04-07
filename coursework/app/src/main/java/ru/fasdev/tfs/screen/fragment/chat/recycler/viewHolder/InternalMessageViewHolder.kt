package ru.fasdev.tfs.screen.fragment.chat.recycler.viewHolder

import android.view.View
import ru.fasdev.tfs.R
import ru.fasdev.tfs.screen.fragment.chat.recycler.viewType.InternalMessageUi
import ru.fasdev.tfs.view.message.base.InternalMessageView

class InternalMessageViewHolder(
    view: View,
    private val reactionListener: OnClickReactionListener,
    private val messageLongListener: OnLongClickMessageListener
) : MessageViewHolder<InternalMessageUi>(view, reactionListener, messageLongListener) {
    private val message: InternalMessageView = view.findViewById(R.id.message)

    override fun bind(item: InternalMessageUi) {
        super.bind(item)

        message.message = item.message
        message.reactions = item.reactions
    }
}
