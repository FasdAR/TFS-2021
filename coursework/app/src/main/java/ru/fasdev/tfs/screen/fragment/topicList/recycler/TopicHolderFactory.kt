package ru.fasdev.tfs.screen.fragment.topicList.recycler

import android.view.View
import ru.fasdev.tfs.R
import ru.fasdev.tfs.screen.fragment.topicList.recycler.viewHolder.StreamViewHolder
import ru.fasdev.tfs.screen.fragment.topicList.recycler.viewHolder.TopicViewHolder
import ru.fasdev.tfs.recycler.viewHolder.ViewHolder
import ru.fasdev.tfs.recycler.viewHolder.ViewHolderFactory

class TopicHolderFactory(
    private val streamListener: StreamViewHolder.OnClickStreamListener,
    private val topicListener: TopicViewHolder.OnClickTopicListener
) : ViewHolderFactory() {
    override fun createViewHolder(view: View, viewType: Int): ViewHolder<*>? {
        return when (viewType) {
            R.layout.item_topic -> StreamViewHolder(view, streamListener)
            R.layout.item_sub_topic -> TopicViewHolder(view, topicListener)
            else -> null
        }
    }
}