package ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewType

import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.ui.global.recycler.base.ViewType

class DateUi(
    override val uId: Int,
    val date: String,
    override val viewType: Int = R.layout.item_date_separation
) : ViewType()
