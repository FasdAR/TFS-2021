package ru.fasdev.tfs.view.ui.fragment.topicList.adapter.diffUtil

import android.os.Bundle
import ru.fasdev.tfs.view.ui.fragment.topicList.adapter.viewHolder.StreamViewHolder
import ru.fasdev.tfs.view.ui.fragment.topicList.adapter.viewType.StreamUi
import ru.fasdev.tfs.view.ui.global.recycler.base.BaseItemCallback
import ru.fasdev.tfs.view.ui.global.recycler.base.ViewType

class TopicItemCallback<T : ViewType> : BaseItemCallback<T>() {
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
