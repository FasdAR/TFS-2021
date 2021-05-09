package ru.fasdev.tfs.recycler.item.date

import android.view.View
import ru.fasdev.tfs.databinding.ItemDateSeparationBinding
import ru.fasdev.tfs.recycler.base.viewHolder.ViewHolder

class DateViewHolder(view: View) : ViewHolder<DateItem>(view) {
    private val binding = ItemDateSeparationBinding.bind(view)

    override fun bind(item: DateItem) {
        binding.date.text = item.date
    }
}
