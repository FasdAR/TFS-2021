package ru.fasdev.tfs.recycler.base.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.fasdev.tfs.recycler.base.diff.ItemCallback
import ru.fasdev.tfs.recycler.base.viewHolder.ViewHolder
import ru.fasdev.tfs.recycler.base.viewHolder.ViewHolderFactory
import ru.fasdev.tfs.recycler.base.viewHolder.ViewType

class RecyclerAdapter<T : ViewType>(
    private val holderFactory: ViewHolderFactory,
    private val itemCallback: DiffUtil.ItemCallback<T> = ItemCallback()
) : RecyclerView.Adapter<ViewHolder<ViewType>>() {

    private val differ = AsyncListDiffer(this, itemCallback)

    var items: List<T>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    var differListListener: AsyncListDiffer.ListListener<T>? = null
        set(value) {
            field?.let { differ.removeListListener(it) }

            field = value
            field?.let { differ.addListListener(it) }
        }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int = items[position].viewType

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<ViewType> {
        return holderFactory(parent, viewType)
    }

    override fun onBindViewHolder(holder: ViewHolder<ViewType>, position: Int) {
        holder.bind(items[position])
    }

    override fun onBindViewHolder(
        holder: ViewHolder<ViewType>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) holder.bind(items[position], payloads)
        else onBindViewHolder(holder, position)
    }
}
