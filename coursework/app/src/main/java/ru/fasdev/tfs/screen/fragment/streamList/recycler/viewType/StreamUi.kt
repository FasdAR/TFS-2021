package ru.fasdev.tfs.screen.fragment.streamList.recycler.viewType

import ru.fasdev.tfs.R
import ru.fasdev.tfs.recycler.viewHolder.ViewType

data class StreamUi(
    override val uId: Int,
    val nameTopic: String,
    var isOpen: Boolean = false,
    override val viewType: Int = R.layout.item_topic
) : ViewType()
