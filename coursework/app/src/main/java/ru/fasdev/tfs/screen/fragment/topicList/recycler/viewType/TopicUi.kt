package ru.fasdev.tfs.screen.fragment.topicList.recycler.viewType

import ru.fasdev.tfs.R
import ru.fasdev.tfs.recycler.viewHolder.ViewType

data class TopicUi(
    override val uId: Int,
    val nameTopic: String,
    val messageCount: Int,
    override val viewType: Int = R.layout.item_sub_topic
) : ViewType()
