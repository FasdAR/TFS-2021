package ru.fasdev.tfs.view.ui.global.recycler.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class HolderFactory : (ViewGroup, Int) -> BaseViewHolder<ViewType> {
    abstract fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>?

    override fun invoke(viewGroup: ViewGroup, viewType: Int): BaseViewHolder<ViewType> {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(viewType, viewGroup, false) as View

        return when (viewType) {
            else -> checkNotNull(createViewHolder(view, viewType)) {
                "Unknow viewType $viewType"
            } as BaseViewHolder<ViewType>
        }
    }
}
