package ru.fasdev.tfs.view.ui.fragment.topicList.adapter.viewHolder

import android.view.View
import androidx.core.content.ContextCompat
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.ItemSubTopicBinding
import ru.fasdev.tfs.view.ui.fragment.topicList.adapter.viewType.SubTopicUi
import ru.fasdev.tfs.view.ui.global.recycler.base.BaseViewHolder

class SubTopicViewHolder(val view: View) : BaseViewHolder<SubTopicUi>(view)
{
    private val binding = ItemSubTopicBinding.bind(view)

    override fun bind(item: SubTopicUi) {
        //TODO: CHNAGE Additional string to res template
        binding.nameTopic.text = item.nameTopic
        binding.msgCount.text = "${item.messageCount} msg"

        if ((item.uId % 2) != 0) {
            binding.root.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.teal_500))
        }
        else {
            binding.root.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.yellow_200))
        }
    }
}