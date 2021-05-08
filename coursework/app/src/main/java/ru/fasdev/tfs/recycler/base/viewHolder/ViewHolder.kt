package ru.fasdev.tfs.recycler.base.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class ViewHolder<T : ViewType>(containerView: View) :
    RecyclerView.ViewHolder(containerView) {

    abstract fun bind(item: T)
    open fun bind(item: T, payloads: List<Any>) = Unit
}
