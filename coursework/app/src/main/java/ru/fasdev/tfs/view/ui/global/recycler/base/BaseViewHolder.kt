package ru.fasdev.tfs.view.ui.global.recycler.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T : ViewType>(private val containerView: View) :
    RecyclerView.ViewHolder(containerView) {

    abstract fun bind(item: T)
    open fun bind(item: T, payloads: List<Any>) = Unit
}
