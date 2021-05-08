package ru.fasdev.tfs.screen.fragment.chat.recycler.diff

import android.os.Bundle
import ru.fasdev.tfs.recycler.base.diff.ItemCallback
import ru.fasdev.tfs.recycler.base.viewHolder.ViewType
import ru.fasdev.tfs.screen.fragment.chat.recycler.viewHolder.MessageViewHolder.Companion.KEY_PAYLOADS_REACTIONS
import ru.fasdev.tfs.screen.fragment.chat.recycler.viewType.MessageUi

class ChatItemCallback<T : ViewType> : ItemCallback<T>() {
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
