package ru.fasdev.tfs.screen.fragment.chat.recycler.viewHolder

import ru.fasdev.tfs.databinding.ItemDateSeparationBinding
import ru.fasdev.tfs.recycler.base.viewHolder.ViewHolder
import ru.fasdev.tfs.screen.fragment.chat.recycler.viewType.DateUi

class DateViewHolder(private val binding: ItemDateSeparationBinding) : ViewHolder<DateUi>(binding.root) {
    override fun bind(item: DateUi) {
        binding.date.text = item.date
    }
}
