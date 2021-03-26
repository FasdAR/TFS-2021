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
) : BaseViewHolder<T>(view), MessageViewGroup.OnClickReactionListener, View.OnLongClickListener {

    interface OnClickReactionListener {
        fun onClickReaction(uIdMessage: Int, emoji: String)
        fun onClickAddNewReaction(uIdMessage: Int)
    }

    fun interface OnLongClickMessageListener {
        fun onLongClickMessage(uIdMessage: Int)
    }

    companion object {
        const val KEY_PAYLOADS_REACTIONS = "REACTIONS"
    }

    private var item: T? = null
    private val messageId: Int
        get() = item?.uId ?: -1

    private val message: MessageViewGroupRoot = view.findViewById(R.id.message)

    init {
        message.onClickReactionListener = this
        message.setOnLongClickListener(this)
    }

    override fun bind(item: T) {
        this.item = item
    }

    override fun bind(item: T, payloads: List<Any>) {
        val bundle = payloads[0] as Bundle
        val newReactions = bundle.getParcelableArrayList<MessageReactionUi>(KEY_PAYLOADS_REACTIONS)

        newReactions?.let {
            message.reactions = it
        }
    }

    override fun onLongClick(v: View?): Boolean {
        messageLongListener.onLongClickMessage(messageId)
        return true
    }

    override fun onClickAddNewReaction() {
        reactionListener.onClickAddNewReaction(messageId)
    }

    override fun onClickReaction(reactionView: ReactionView, emoji: String) {
        reactionListener.onClickReaction(messageId, emoji)
    }
}
