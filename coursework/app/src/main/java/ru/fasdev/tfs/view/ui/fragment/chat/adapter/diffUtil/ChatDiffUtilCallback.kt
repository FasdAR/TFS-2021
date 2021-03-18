package ru.fasdev.tfs.view.ui.fragment.chat.adapter.diffUtil

import android.os.Bundle
import ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewHolder.MessageViewHolder.Companion.KEY_REACTION_PAYLOADS
import ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewType.MessageUi
import ru.fasdev.tfs.view.ui.global.recycler.base.BaseDiffUtilCallback
import ru.fasdev.tfs.view.ui.global.recycler.base.ViewType


class ChatDiffUtilCallback<T : ViewType> : BaseDiffUtilCallback<T>()
{
    override fun getChangePayload(oldItem: T, newItem: T): Any? {
        if (oldItem is MessageUi && newItem is MessageUi) {
            val diffBundle = Bundle()

            if (oldItem.reactions != newItem.reactions) {
                diffBundle.putParcelableArrayList(KEY_REACTION_PAYLOADS, ArrayList(newItem.reactions))
            }

            return diffBundle
        }

        return super.getChangePayload(oldItem, newItem)
    }
}