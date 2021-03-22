package ru.fasdev.tfs.view.ui.fragment.channels.adapter.viewHolder

import android.view.View
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.ItemTopicBinding
import ru.fasdev.tfs.view.ui.fragment.channels.adapter.viewType.TopicUi
import ru.fasdev.tfs.view.ui.global.recycler.base.BaseViewHolder

class TopicViewHolder(val view: View) : BaseViewHolder<TopicUi>(view)
{
    private val binding = ItemTopicBinding.bind(view)

    override fun bind(item: TopicUi) {
        binding.nameTopic.text = item.nameTopic

        if (item.isOpen) binding.arrowOpenClose.setImageResource(R.drawable.ic_arrow_up)
        else binding.arrowOpenClose.setImageResource(R.drawable.ic_arrow_down)
    }
}