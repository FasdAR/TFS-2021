package ru.fasdev.tfs.recycler.item.topic

import android.view.View
import ru.fasdev.tfs.R
import ru.fasdev.tfs.core.ext.getColorCompat
import ru.fasdev.tfs.databinding.ItemSubTopicBinding
import ru.fasdev.tfs.recycler.base.viewHolder.ViewHolder

class TopicViewHolder(val view: View, private val onClickSubTopicListener: OnClickTopicListener) : ViewHolder<TopicItem>(view) {
    private val binding = ItemSubTopicBinding.bind(view)

    interface OnClickTopicListener {
        fun onClickTopic(nameTopic: String, idStream: Long)
    }

    override fun bind(item: TopicItem) {
        binding.nameTopic.text = item.nameTopic
        binding.msgCount.text = binding.root.resources.getString(R.string.sub_topic_msg, item.messageCount.toString())

        binding.root.setOnClickListener { onClickSubTopicListener.onClickTopic(item.nameTopic, item.idStream) }
        val isEvenNumber = (item.uId % 2) != 0

        if (isEvenNumber) {
            binding.root.setBackgroundColor(binding.root.context.getColorCompat(R.color.teal_500))
        } else {
            binding.root.setBackgroundColor(binding.root.context.getColorCompat(R.color.yellow_200))
        }
    }
}
