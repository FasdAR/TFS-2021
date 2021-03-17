package ru.fasdev.tfs.view.ui.global.recycler.base

import androidx.recyclerview.widget.DiffUtil

class BaseDiffUtilCallback<T : ViewType> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = oldItem.uId == newItem.uId

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
}
