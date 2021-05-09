package ru.fasdev.tfs.recycler.item.date

import ru.fasdev.tfs.R
import ru.fasdev.tfs.recycler.base.viewHolder.ViewType

class DateItem(
    override val uId: Int,
    val date: String,
    override val viewType: Int = R.layout.item_date_separation
) : ViewType()
