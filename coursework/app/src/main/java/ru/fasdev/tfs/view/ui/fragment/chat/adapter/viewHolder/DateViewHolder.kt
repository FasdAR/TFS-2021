package ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewHolder

import android.view.View
import android.widget.TextView
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.ItemDateSeparationBinding
import ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewType.DateUi
import ru.fasdev.tfs.view.ui.global.recycler.base.BaseViewHolder

class DateViewHolder(private val viewBinding: ItemDateSeparationBinding) : BaseViewHolder<DateUi>(viewBinding.root) {
    override fun bind(item: DateUi) {
        viewBinding.date.text = item.date
    }
}
