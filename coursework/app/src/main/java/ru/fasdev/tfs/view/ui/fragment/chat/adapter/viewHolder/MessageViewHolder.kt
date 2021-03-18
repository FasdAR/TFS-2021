package ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewHolder

import android.os.Bundle
import android.view.View
import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewType.MessageUi
import ru.fasdev.tfs.view.ui.global.recycler.base.BaseViewHolder
import ru.fasdev.tfs.view.ui.global.view.view.ReactionView
import ru.fasdev.tfs.view.ui.global.view.viewGroup.message.MessageViewGroup
import ru.fasdev.tfs.view.ui.global.view.viewGroup.message.MessageViewGroupRoot
import ru.fasdev.tfs.view.ui.global.view.viewGroup.message.model.MessageReactionUi

abstract class MessageViewHolder<T : MessageUi>(
    val view: View,
    private val reactionListener: OnClickReactionListener,
    private val messageLongListener: OnLongClickMessageListener
) :
    BaseViewHolder<T>(view), MessageViewGroup.OnClickReactionListener, View.OnLongClickListener {
    companion object {
        const val KEY_REACTION_PAYLOADS = "REACTIONS"
    }
    private var currentMessageId: Int = -1

    private val message: MessageViewGroupRoot = view.findViewById(R.id.message)

    override fun bind(item: T) {
        currentMessageId = item.uId

        message.onClickReactionListener = this
        message.setOnLongClickListener(this)
    }

    override fun bind(item: T, payloads: List<Any>) {
        val bundle = payloads[0] as Bundle
        val newReactions = bundle.getParcelableArrayList<MessageReactionUi>(KEY_REACTION_PAYLOADS)

        newReactions?.let {
            message.reactions = it
        }
    }

    interface OnClickReactionListener {
        fun onClickReaction(uIdMessage: Int, emoji: String)
        fun onClickAddNewReaction(uIdMessage: Int)
    }

    interface OnLongClickMessageListener {
        fun onLongClickMessage(uIdMessage: Int)
    }

    override fun onLongClick(v: View?): Boolean {
        messageLongListener.onLongClickMessage(currentMessageId)
        return true
    }

    override fun onClickAddNewReaction() {
        reactionListener.onClickAddNewReaction(currentMessageId)
    }

    override fun onClickReaction(reactionView: ReactionView, emoji: String) {
        reactionListener.onClickReaction(currentMessageId, emoji)
    }
}
