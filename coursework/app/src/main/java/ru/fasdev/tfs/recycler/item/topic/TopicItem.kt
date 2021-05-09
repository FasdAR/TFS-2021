package ru.fasdev.tfs.recycler.item.topic

import ru.fasdev.tfs.R
import ru.fasdev.tfs.recycler.base.viewHolder.ViewType

data class TopicItem(
    override val uId: Int,
    val idStream: Long,
    val nameTopic: String,
    val messageCount: Int,
    override val viewType: Int = R.layout.item_sub_topic
) : ViewType()
