package ru.fasdev.tfs.view.feature.recycler

import androidx.recyclerview.widget.DiffUtil
import ru.fasdev.tfs.view.feature.recycler.base.ViewTyped

class BaseDiffUtil<T : ViewTyped> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = oldItem.uId == newItem.uId

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
}
