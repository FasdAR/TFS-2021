package ru.fasdev.tfs.view.feature.recycler.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T: ViewTyped>(private val containerView: View) :
        RecyclerView.ViewHolder(containerView)
{
    abstract fun bind(item: T)
}