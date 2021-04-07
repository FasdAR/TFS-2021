package ru.fasdev.tfs.screen.fragment.topicList.recycler.diuff

import android.os.Bundle
import ru.fasdev.tfs.screen.fragment.topicList.recycler.viewHolder.StreamViewHolder
import ru.fasdev.tfs.screen.fragment.topicList.recycler.viewType.StreamUi
import ru.fasdev.tfs.recycler.diff.ItemCallback
import ru.fasdev.tfs.recycler.viewHolder.ViewType

class TopicItemCallback<T : ViewType> : ItemCallback<T>() {
    override fun getChangePayload(oldItem: T, newItem: T): Any? {
        if (oldItem is StreamUi && newItem is StreamUi) {
            val diffBundle = Bundle()

            if (oldItem.isOpen != newItem.isOpen) {
                diffBundle.putBoolean(StreamViewHolder.KEY_PAYLOADS_IS_OPEN, newItem.isOpen)
            }

            return diffBundle
        }
        return super.getChangePayload(oldItem, newItem)
    }
}
