package ru.fasdev.tfs.screen.fragment.chat.recycler.viewHolder

import android.os.Bundle
import android.view.View
import ru.fasdev.tfs.R
import ru.fasdev.tfs.recycler.base.viewHolder.ViewHolder
import ru.fasdev.tfs.screen.fragment.chat.recycler.viewType.MessageUi
import ru.fasdev.tfs.view.message.MessageViewGroup
import ru.fasdev.tfs.view.message.base.BaseMessageView
import ru.fasdev.tfs.view.message.base.model.MessageReactionUi
import ru.fasdev.tfs.view.reaction.ReactionView

abstract class MessageViewHolder<T : MessageUi>(
    val view: View,
    private val reactionListener: OnClickReactionListener,
    private val messageLongListener: OnLongClickMessageListener
) : ViewHolder<T>(view), MessageViewGroup.OnClickReactionListener, View.OnLongClickListener {

    interface OnClickReactionListener {
        fun onClickReaction(uIdMessage: Int, emoji: String, isSelected: Boolean)
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

    private val message: BaseMessageView = view.findViewById(R.id.message)

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

    override fun onClickReaction(reactionView: ReactionView, emoji: String, isSelected: Boolean) {
        reactionListener.onClickReaction(messageId, emoji, isSelected)
    }
}
