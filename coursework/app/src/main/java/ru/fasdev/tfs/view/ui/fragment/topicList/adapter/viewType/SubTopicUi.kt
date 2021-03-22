package ru.fasdev.tfs.view.ui.fragment.topicList.adapter.viewType

import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.ui.global.recycler.base.ViewType

data class SubTopicUi(
    override val uId: Int,
    val nameTopic: String,
    val messageCount: Int,
    override val viewType: Int = R.layout.item_sub_topic
): ViewType()