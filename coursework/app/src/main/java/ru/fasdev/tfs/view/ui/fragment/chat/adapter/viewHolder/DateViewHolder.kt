package ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewHolder

import android.view.View
import android.widget.TextView
import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.ui.global.recycler.base.BaseViewHolder
import ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewType.DateUi

class DateViewHolder(view: View) : BaseViewHolder<DateUi>(view) {
    val dateText: TextView = view.findViewById(R.id.date)

    override fun bind(item: DateUi) {
        dateText.text = item.date
    }
}
