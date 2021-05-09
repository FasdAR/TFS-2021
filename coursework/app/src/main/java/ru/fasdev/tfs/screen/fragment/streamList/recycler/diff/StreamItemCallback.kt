package ru.fasdev.tfs.screen.fragment.streamList.recycler.diff

import android.os.Bundle
import ru.fasdev.tfs.recycler.base.diff.ItemCallback
import ru.fasdev.tfs.recycler.base.viewHolder.ViewType
import ru.fasdev.tfs.recycler.item.stream.StreamViewHolder
import ru.fasdev.tfs.recycler.item.stream.StreamItem

class StreamItemCallback<T : ViewType> : ItemCallback<T>() {
    override fun getChangePayload(oldItem: T, newItem: T): Any? {
        if (oldItem is StreamItem && newItem is StreamItem) {
            val diffBundle = Bundle()

            if (oldItem.isOpen != newItem.isOpen) {
                diffBundle.putBoolean(StreamViewHolder.KEY_PAYLOADS_IS_OPEN, newItem.isOpen)
            }

            return diffBundle
        }
        return super.getChangePayload(oldItem, newItem)
    }
}
