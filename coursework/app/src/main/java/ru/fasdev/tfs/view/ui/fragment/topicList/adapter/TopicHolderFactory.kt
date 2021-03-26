package ru.fasdev.tfs.view.ui.fragment.topicList.adapter

import android.view.View
import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.ui.fragment.topicList.adapter.viewHolder.TopicViewHolder
import ru.fasdev.tfs.view.ui.fragment.topicList.adapter.viewHolder.StreamViewHolder
import ru.fasdev.tfs.view.ui.global.recycler.base.BaseViewHolder
import ru.fasdev.tfs.view.ui.global.recycler.base.HolderFactory

class TopicHolderFactory(
        private val streamListener: StreamViewHolder.OnClickStreamListener,
        private val topicListener: TopicViewHolder.OnClickTopicListener
) : HolderFactory() {
    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>? {
        return when (viewType) {
            R.layout.item_topic -> StreamViewHolder(view, streamListener)
            R.layout.item_sub_topic -> TopicViewHolder(view, topicListener)
            else -> null
        }
    }
}
