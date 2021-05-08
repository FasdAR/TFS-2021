package ru.fasdev.tfs.screen.fragment.streamList.recycler.viewType

import ru.fasdev.tfs.R
import ru.fasdev.tfs.recycler.base.viewHolder.ViewType

data class TopicUi(
    override val uId: Int,
    val streamName: String,
    val nameTopic: String,
    val messageCount: Int,
    override val viewType: Int = R.layout.item_sub_topic
) : ViewType()
