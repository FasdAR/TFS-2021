package ru.fasdev.tfs.recycler.item.emptySearch

import ru.fasdev.tfs.R
import ru.fasdev.tfs.recycler.base.viewHolder.ViewType

data class EmptySearchItem (
    override val uId: Int,
    val text: String? = null,
    override val viewType: Int = R.layout.item_empty_res_search
) : ViewType()