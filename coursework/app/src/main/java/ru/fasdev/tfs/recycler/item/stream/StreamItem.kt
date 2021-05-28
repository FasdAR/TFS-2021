package ru.fasdev.tfs.recycler.item.stream

import ru.fasdev.tfs.R
import ru.fasdev.tfs.recycler.base.viewHolder.ViewType

data class StreamItem(
    override val uId: Int,
    val nameTopic: String,
    var isOpen: Boolean = false,
    override val viewType: Int = R.layout.item_topic
) : ViewType()
