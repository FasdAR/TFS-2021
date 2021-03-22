package ru.fasdev.tfs.view.ui.fragment.topicList.adapter

import android.view.View
import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.ui.fragment.topicList.adapter.viewHolder.SubTopicViewHolder
import ru.fasdev.tfs.view.ui.fragment.topicList.adapter.viewHolder.TopicViewHolder
import ru.fasdev.tfs.view.ui.global.recycler.base.BaseViewHolder
import ru.fasdev.tfs.view.ui.global.recycler.base.HolderFactory

class TopicHolderFactory : HolderFactory()
{
    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>? {
        return when(viewType) {
            R.layout.item_topic -> TopicViewHolder(view)
            R.layout.item_sub_topic -> SubTopicViewHolder(view)
            else -> null
        }
    }
}