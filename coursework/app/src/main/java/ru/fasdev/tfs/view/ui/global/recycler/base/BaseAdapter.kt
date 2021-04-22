package ru.fasdev.tfs.view.ui.global.recycler.base

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class BaseAdapter<T : ViewType>(
    private val holderFactory: HolderFactory,
    itemCallback: DiffUtil.ItemCallback<T> = BaseDiffUtilCallback(),
    asyncListDiffer: AsyncListDiffer.ListListener<T>? = null
) : RecyclerView.Adapter<BaseViewHolder<ViewType>>() {

    private val differ = AsyncListDiffer(this, itemCallback)

    var items: List<T>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    init {
        asyncListDiffer?.let {
            differ.addListListener(asyncListDiffer)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ViewType> =
        holderFactory(parent, viewType)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: BaseViewHolder<ViewType>, position: Int) {
        holder.bind(items[position])
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<ViewType>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            holder.bind(items[position], payloads)
        } else {
            onBindViewHolder(holder, position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].viewType
    }
}
