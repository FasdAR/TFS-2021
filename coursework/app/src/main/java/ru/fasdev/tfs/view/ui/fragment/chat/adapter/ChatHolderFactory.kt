package ru.fasdev.tfs.view.ui.fragment.chat.adapter

import android.view.View
import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.feature.customView.viewGroup.message.MessageViewGroup
import ru.fasdev.tfs.view.feature.recycler.base.BaseViewHolder
import ru.fasdev.tfs.view.feature.recycler.base.HolderFactory
import ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewHolders.DateViewHolder
import ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewHolders.ExternalMessageViewHolder
import ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewHolders.InternalMessageViewHolder

class ChatHolderFactory(
        private val onLongClickMessageListener: OnLongClickMessageListener,
        private val onClickReactionListener: OnClickReactionListener,
        private val onClickPlusReactionListener: MessageViewGroup.OnClickPlusReactionListener): HolderFactory()
{
    interface OnClickReactionListener {
        fun onClickReaction(uIdMessage: Int, emoji: String)
    }

    interface OnLongClickMessageListener {
        fun onLongClickMessage(uIdMessage: Int)
    }

    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>? {
        return when(viewType) {
            R.layout.item_date_separation -> DateViewHolder(view)
            R.layout.item_external_message -> ExternalMessageViewHolder(view).apply {
                onClickReactionListener = this@ChatHolderFactory.onClickReactionListener
                onClickPlusReactionListener = this@ChatHolderFactory.onClickPlusReactionListener
                onLongClickMessageListener = this@ChatHolderFactory.onLongClickMessageListener
            }
            R.layout.item_internal_message -> InternalMessageViewHolder(view).apply {
                onClickReactionListener = this@ChatHolderFactory.onClickReactionListener
                onClickPlusReactionListener = this@ChatHolderFactory.onClickPlusReactionListener
                onLongClickMessageListener = this@ChatHolderFactory.onLongClickMessageListener
            }
            else -> null
        }
    }
}