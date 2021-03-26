package ru.fasdev.tfs.view.ui.fragment.chat.adapter.diffUtil

import android.os.Bundle
import ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewHolder.MessageViewHolder.Companion.KEY_PAYLOADS_REACTIONS
import ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewType.MessageUi
import ru.fasdev.tfs.view.ui.global.recycler.base.BaseItemCallback
import ru.fasdev.tfs.view.ui.global.recycler.base.ViewType

class ChatItemCallback<T : ViewType> : BaseItemCallback<T>() {
    override fun getChangePayload(oldItem: T, newItem: T): Any? {
        if (oldItem is MessageUi && newItem is MessageUi) {
            val diffBundle = Bundle()

            if (oldItem.reactions != newItem.reactions) {
                diffBundle.putParcelableArrayList(KEY_PAYLOADS_REACTIONS, ArrayList(newItem.reactions))
            }

            return diffBundle
        }

        return super.getChangePayload(oldItem, newItem)
    }
}
