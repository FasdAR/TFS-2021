package ru.fasdev.tfs.view.ui.global.recycler.base

import androidx.recyclerview.widget.DiffUtil

open class BaseItemCallback<T : ViewType> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        if (oldItem::class.simpleName == newItem::class.simpleName) {
            return oldItem.uId == newItem.uId
        }

        return false
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
}
