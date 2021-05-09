package ru.fasdev.tfs.recycler.item.stream

import android.os.Bundle
import android.view.View
import ru.fasdev.tfs.R
import ru.fasdev.tfs.core.ext.getColorCompat
import ru.fasdev.tfs.databinding.ItemTopicBinding
import ru.fasdev.tfs.recycler.base.viewHolder.ViewHolder

class StreamViewHolder(val view: View, private val streamListener: OnClickStreamListener) : ViewHolder<StreamItem>(view) {
    companion object {
        const val KEY_PAYLOADS_IS_OPEN = "ID_OPEN"
    }

    fun interface OnClickStreamListener {
        fun onClickStream(idStream: Int, opened: Boolean)
    }

    private val binding = ItemTopicBinding.bind(view)
    private var isOpened: Boolean = false

    private var item: StreamItem? = null

    init {
        binding.root.setOnClickListener {
            item?.let { streamListener.onClickStream(it.uId, !isOpened) }
        }
    }

    override fun bind(item: StreamItem) {
        this.item = item

        binding.nameTopic.text = view.resources.getString(R.string.main_topic_title, item.nameTopic)
        isOpened = item.isOpen

        setOpenArrow(isOpened)
    }

    override fun bind(item: StreamItem, payloads: List<Any>) {
        val bundle = payloads[0] as Bundle
        isOpened = bundle.getBoolean(KEY_PAYLOADS_IS_OPEN)
        setOpenArrow(isOpened)

        super.bind(item, payloads)
    }

    private fun setOpenArrow(isOpen: Boolean) {
        if (isOpen) {
            binding.arrowOpenClose.setImageResource(R.drawable.ic_arrow_up)
            binding.root.setBackgroundColor(binding.root.context.getColorCompat(R.color.grey_920))
        } else {
            binding.arrowOpenClose.setImageResource(R.drawable.ic_arrow_down)
            binding.root.setBackgroundColor(0)
        }
    }
}
