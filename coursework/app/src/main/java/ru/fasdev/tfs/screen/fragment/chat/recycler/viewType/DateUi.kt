package ru.fasdev.tfs.screen.fragment.chat.recycler.viewType

import ru.fasdev.tfs.R
import ru.fasdev.tfs.recycler.viewHolder.ViewType

data class DateUi(
    override val uId: Int,
    val date: String,
    override val viewType: Int = R.layout.item_date_separation
) : ViewType()
