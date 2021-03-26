package ru.fasdev.tfs.view.ui.fragment.topicList.adapter.viewHolder

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.ItemTopicBinding
import ru.fasdev.tfs.view.ui.fragment.topicList.adapter.viewType.TopicUi
import ru.fasdev.tfs.view.ui.global.recycler.base.BaseViewHolder

class TopicViewHolder(val view: View, val topicListener: OnClickTopicListener) : BaseViewHolder<TopicUi>(view) {
    companion object {
        const val KEY_IS_OPEN_PAYLOADS = "IDOPEN"
    }

    interface OnClickTopicListener {
        fun onClickTopic(idTopic: Int, opened: Boolean)
    }

    private val binding = ItemTopicBinding.bind(view)
    private var isOpened: Boolean = false

    override fun bind(item: TopicUi) {
        // TODO: FIX ADDED TEMPLATE STRING
        binding.nameTopic.text = "#${item.nameTopic}"
        isOpened = item.isOpen

        setOpenArrow(isOpened)

        binding.root.setOnClickListener {
            topicListener.onClickTopic(item.uId, !isOpened)
        }
    }

    override fun bind(item: TopicUi, payloads: List<Any>) {
        val bundle = payloads[0] as Bundle
        isOpened = bundle.getBoolean(KEY_IS_OPEN_PAYLOADS)

        setOpenArrow(isOpened)

        super.bind(item, payloads)
    }

    private fun setOpenArrow(isOpen: Boolean) {
        if (isOpen) {
            binding.arrowOpenClose.setImageResource(R.drawable.ic_arrow_up)
            binding.root.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.grey_920))
        } else {
            binding.arrowOpenClose.setImageResource(R.drawable.ic_arrow_down)
            binding.root.setBackgroundColor(0)
        }
    }
}
