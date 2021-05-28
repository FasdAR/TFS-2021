package ru.fasdev.tfs.recycler.item.emptySearch

import android.view.View
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.ItemEmptyResSearchBinding
import ru.fasdev.tfs.recycler.base.viewHolder.ViewHolder

class EmptySearchViewHolder(val view: View) : ViewHolder<EmptySearchItem>(view) {
    private val binding = ItemEmptyResSearchBinding.bind(view)

    override fun bind(item: EmptySearchItem) {
        item.text?.let {
            binding.text.text = it
        } ?: kotlin.run {
            binding.text.text = view.resources.getString(R.string.empty_res_search)
        }
    }
}
