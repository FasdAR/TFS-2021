package ru.fasdev.tfs.view.ui.fragment.adapter.viewTypes

import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.feature.recycler.base.ViewTyped

class DateUi(
        val date: String,
        override val viewType: Int = R.layout.layout_date_separation,
        override val uId: Int
): ViewTyped()