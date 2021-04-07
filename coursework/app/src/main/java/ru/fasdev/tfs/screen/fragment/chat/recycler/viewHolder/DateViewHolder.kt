package ru.fasdev.tfs.screen.fragment.chat.recycler.viewHolder

import android.view.View
import ru.fasdev.tfs.databinding.ItemDateSeparationBinding
import ru.fasdev.tfs.screen.fragment.chat.recycler.viewType.DateUi
import ru.fasdev.tfs.recycler.viewHolder.ViewHolder

class DateViewHolder(view: View) : ViewHolder<DateUi>(view) {
    private val binding = ItemDateSeparationBinding.bind(view)

    override fun bind(item: DateUi) {
        binding.date.text = item.date
    }
}
