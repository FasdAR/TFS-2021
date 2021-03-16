package ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewHolders

import android.view.View
import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.feature.customView.view.ReactionView
import ru.fasdev.tfs.view.feature.customView.viewGroup.message.ExternalMessageViewGroup
import ru.fasdev.tfs.view.feature.customView.viewGroup.message.MessageViewGroup
import ru.fasdev.tfs.view.feature.recycler.base.BaseViewHolder
import ru.fasdev.tfs.view.ui.fragment.chat.adapter.ChatHolderFactory
import ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewTypes.ExternalMessageUi

class ExternalMessageViewHolder(view: View,) : BaseViewHolder<ExternalMessageUi>(view) {
    val message: ExternalMessageViewGroup = view.findViewById(R.id.message)

    var onLongClickMessageListener: ChatHolderFactory.OnLongClickMessageListener? = null
    var onClickReactionListener: ChatHolderFactory.OnClickReactionListener? = null
    var onClickPlusReactionListener: MessageViewGroup.OnClickPlusReactionListener? = null

    override fun bind(item: ExternalMessageUi) {
        message.avatarSrc = item.avatarSrc
        message.name = item.nameSender
        message.message = item.message
        message.reactions = item.reactions

        message.onClickReactionListener = object : MessageViewGroup.OnClickReactionListener {
            override fun onClickReaction(reactionView: ReactionView, emoji: String) {
                onClickReactionListener?.onClickReaction(item.uId, emoji)
            }
        }
        message.onClickPlusReactionListener = onClickPlusReactionListener
        message.setOnLongClickListener {
            onLongClickMessageListener?.onLongClickMessage(item.uId)
            true
        }
    }
}
