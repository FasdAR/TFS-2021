package ru.fasdev.tfs.view.ui.fragment.channels.adapter.viewType

import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.ui.global.recycler.base.ViewType

data class TopicUi(
    override val uId: Int,
    val nameTopic: String,
    val isOpen: Boolean,
    override val viewType: Int = R.layout.item_topic
): ViewType()