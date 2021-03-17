package ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewHolder

import android.view.View
import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.ui.global.view.viewGroup.message.ExternalMessageViewGroup
import ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewType.ExternalMessageUi

class ExternalMessageViewHolder(view: View,
                                private val reactionListener: OnClickReactionListener,
                                private val messageLongListener: OnLongClickMessageListener)
    : MessageViewHolder<ExternalMessageUi>(view, reactionListener, messageLongListener) {

    val message: ExternalMessageViewGroup = view.findViewById(R.id.message)

    override fun bind(item: ExternalMessageUi) {
        super.bind(item)

        message.avatarSrc = item.avatarSrc
        message.name = item.nameSender
        message.message = item.message
        message.reactions = item.reactions
    }
}
