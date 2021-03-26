package ru.fasdev.tfs.view.ui.fragment.topicList.adapter.diffUtil

import android.os.Bundle
import ru.fasdev.tfs.view.ui.fragment.topicList.adapter.viewHolder.TopicViewHolder
import ru.fasdev.tfs.view.ui.fragment.topicList.adapter.viewType.TopicUi
import ru.fasdev.tfs.view.ui.global.recycler.base.BaseDiffUtilCallback
import ru.fasdev.tfs.view.ui.global.recycler.base.ViewType

class TopicDiffUtilCallback<T : ViewType> : BaseDiffUtilCallback<T>() {
    override fun getChangePayload(oldItem: T, newItem: T): Any? {
        if (oldItem is TopicUi && newItem is TopicUi) {
            val diffBundle = Bundle()

            if (oldItem.isOpen != newItem.isOpen) {
                diffBundle.putBoolean(TopicViewHolder.KEY_IS_OPEN_PAYLOADS, newItem.isOpen)
            }

            return diffBundle
        }
        return super.getChangePayload(oldItem, newItem)
    }
}
