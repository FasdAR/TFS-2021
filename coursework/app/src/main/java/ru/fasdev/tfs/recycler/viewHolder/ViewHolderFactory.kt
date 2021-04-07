package ru.fasdev.tfs.recycler.viewHolder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class ViewHolderFactory : (ViewGroup, Int) -> ViewHolder<ViewType> {
    abstract fun createViewHolder(view: View, viewType: Int): ViewHolder<*>?

    override fun invoke(viewGroup: ViewGroup, viewType: Int): ViewHolder<ViewType> {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(viewType, viewGroup, false) as View

        val holder = createViewHolder(view, viewType)

        if (holder == null) error("Unknown ViewType $viewType")
        else return holder as ViewHolder<ViewType>
    }
}
