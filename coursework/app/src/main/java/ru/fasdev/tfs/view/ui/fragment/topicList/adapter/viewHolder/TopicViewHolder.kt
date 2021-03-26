package ru.fasdev.tfs.view.ui.fragment.topicList.adapter.viewHolder

import android.view.View
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.ItemSubTopicBinding
import ru.fasdev.tfs.view.feature.util.getColorCompat
import ru.fasdev.tfs.view.ui.fragment.topicList.adapter.viewType.TopicUi
import ru.fasdev.tfs.view.ui.global.recycler.base.BaseViewHolder

class TopicViewHolder(val view: View, private val onClickSubTopicListener: OnClickTopicListener) : BaseViewHolder<TopicUi>(view) {
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
