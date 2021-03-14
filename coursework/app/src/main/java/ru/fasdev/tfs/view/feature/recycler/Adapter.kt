package ru.fasdev.tfs.view.feature.recycler

import ru.fasdev.tfs.view.feature.recycler.base.BaseAdapter
import ru.fasdev.tfs.view.feature.recycler.base.HolderFactory
import ru.fasdev.tfs.view.feature.recycler.base.ViewTyped

class Adapter<T: ViewTyped>(holderFactory: HolderFactory) : BaseAdapter<T>(holderFactory)
{
    private val localItems: MutableList<T> = mutableListOf()

    override var items: List<T>
        get() = localItems
        set(value) {
            localItems.clear()
            localItems.addAll(value)
            notifyDataSetChanged()
        }
}