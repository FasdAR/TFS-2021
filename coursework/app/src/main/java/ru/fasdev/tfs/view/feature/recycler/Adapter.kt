package ru.fasdev.tfs.view.feature.recycler

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import ru.fasdev.tfs.view.feature.recycler.base.BaseAdapter
import ru.fasdev.tfs.view.feature.recycler.base.HolderFactory
import ru.fasdev.tfs.view.feature.recycler.base.ViewTyped

class Adapter<T : ViewTyped>(
    holderFactory: HolderFactory,
    private val itemCallback: DiffUtil.ItemCallback<T> = BaseDiffUtil()
) :
    BaseAdapter<T>(holderFactory) {
    private val differ = AsyncListDiffer(this, itemCallback)

    override var items: List<T>
        get() = differ.currentList
        set(value) = differ.submitList(value)
}
