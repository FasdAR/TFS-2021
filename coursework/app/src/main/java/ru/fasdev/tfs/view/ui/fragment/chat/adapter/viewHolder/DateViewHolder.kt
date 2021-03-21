package ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewHolder

import android.view.View
import ru.fasdev.tfs.databinding.ItemDateSeparationBinding
import ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewType.DateUi
import ru.fasdev.tfs.view.ui.global.recycler.base.BaseViewHolder

class DateViewHolder(view: View) : BaseViewHolder<DateUi>(view) {
    private val binding = ItemDateSeparationBinding.bind(view)

    override fun bind(item: DateUi) {
        binding.date.text = item.date
    }
}
