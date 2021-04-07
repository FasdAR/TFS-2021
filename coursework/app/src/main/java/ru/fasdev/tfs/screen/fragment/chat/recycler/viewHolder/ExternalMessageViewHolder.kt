package ru.fasdev.tfs.screen.fragment.chat.recycler.viewHolder

import android.view.View
import ru.fasdev.tfs.R
import ru.fasdev.tfs.screen.fragment.chat.recycler.viewType.ExternalMessageUi
import ru.fasdev.tfs.view.message.base.ExternalMessageView

class ExternalMessageViewHolder(
    view: View,
    private val reactionListener: OnClickReactionListener,
    private val messageLongListener: OnLongClickMessageListener
) : MessageViewHolder<ExternalMessageUi>(view, reactionListener, messageLongListener) {

    val message: ExternalMessageView = view.findViewById(R.id.message)

    override fun bind(item: ExternalMessageUi) {
        super.bind(item)

        message.avatarSrc = item.avatarSrc
        message.name = item.nameSender
        message.message = item.message
        message.reactions = item.reactions
    }
}
