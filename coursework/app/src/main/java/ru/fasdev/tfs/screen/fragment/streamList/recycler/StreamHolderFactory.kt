package ru.fasdev.tfs.screen.fragment.streamList.recycler

import android.view.View
import ru.fasdev.tfs.R
import ru.fasdev.tfs.recycler.base.viewHolder.ViewHolder
import ru.fasdev.tfs.recycler.base.viewHolder.ViewHolderFactory
import ru.fasdev.tfs.screen.fragment.streamList.recycler.viewHolder.StreamViewHolder
import ru.fasdev.tfs.screen.fragment.streamList.recycler.viewHolder.TopicViewHolder

class StreamHolderFactory(
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
