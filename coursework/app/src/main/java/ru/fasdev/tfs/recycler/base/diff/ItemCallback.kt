package ru.fasdev.tfs.recycler.base.diff

import androidx.recyclerview.widget.DiffUtil
import ru.fasdev.tfs.recycler.base.viewHolder.ViewType

open class ItemCallback<T : ViewType> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        if (oldItem::class.simpleName == newItem::class.simpleName) {
            return oldItem.uId == newItem.uId
        }

        return false
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
}
