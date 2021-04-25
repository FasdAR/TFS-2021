package ru.fasdev.tfs.screen.fragment.chat.recycler.viewHolder

import ru.fasdev.tfs.databinding.ItemDateSeparationBinding
import ru.fasdev.tfs.recycler.viewHolder.ViewHolder
import ru.fasdev.tfs.screen.fragment.chat.recycler.viewType.DateUi

class DateViewHolder(private val viewBinding: ItemDateSeparationBinding) : ViewHolder<DateUi>(viewBinding.root) {

    override fun bind(item: DateUi) {
        viewBinding.date.text = item.date
    }
}
