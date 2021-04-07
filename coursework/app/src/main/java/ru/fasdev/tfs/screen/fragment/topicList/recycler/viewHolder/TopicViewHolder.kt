package ru.fasdev.tfs.screen.fragment.topicList.recycler.viewHolder

import android.view.View
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.ItemSubTopicBinding
import ru.fasdev.tfs.core.ext.getColorCompat
import ru.fasdev.tfs.screen.fragment.topicList.recycler.viewType.TopicUi
import ru.fasdev.tfs.recycler.viewHolder.ViewHolder

class TopicViewHolder(val view: View, private val onClickSubTopicListener: OnClickTopicListener) : ViewHolder<TopicUi>(view) {
    private val binding = ItemSubTopicBinding.bind(view)

    interface OnClickTopicListener {
        fun onClickTopic(idTopic: Int)
    }

    override fun bind(item: TopicUi) {
        binding.nameTopic.text = item.nameTopic
        binding.msgCount.text = binding.root.resources.getString(R.string.sub_topic_msg, item.messageCount.toString())

        binding.root.setOnClickListener { onClickSubTopicListener.onClickTopic(item.uId) }

        if ((item.uId % 2) != 0) {
            binding.root.setBackgroundColor(binding.root.context.getColorCompat(R.color.teal_500))
        } else {
            binding.root.setBackgroundColor(binding.root.context.getColorCompat(R.color.yellow_200))
        }
    }
}
