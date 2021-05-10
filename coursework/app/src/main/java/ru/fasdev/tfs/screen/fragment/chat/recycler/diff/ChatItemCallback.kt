package ru.fasdev.tfs.screen.fragment.chat.recycler.diff

import android.os.Bundle
import ru.fasdev.tfs.recycler.base.diff.ItemCallback
import ru.fasdev.tfs.recycler.base.viewHolder.ViewType
import ru.fasdev.tfs.recycler.item.message.MessageItem
import ru.fasdev.tfs.recycler.item.message.MessageViewHolder.Companion.KEY_PAYLOADS_REACTIONS

class ChatItemCallback<T : ViewType> : ItemCallback<T>() {
    override fun getChangePayload(oldItem: T, newItem: T): Any? {
        if (oldItem is MessageItem && newItem is MessageItem) {
            val diffBundle = Bundle()

            if (oldItem.reactions != newItem.reactions) {
                diffBundle.putParcelableArrayList(KEY_PAYLOADS_REACTIONS, ArrayList(newItem.reactions))
            }

            return diffBundle
        }

        return super.getChangePayload(oldItem, newItem)
    }
}
