package ru.fasdev.tfs.view.ui.fragment.adapter

import android.view.View
import ru.fasdev.tfs.view.feature.recycler.base.BaseViewHolder
import ru.fasdev.tfs.view.feature.recycler.base.HolderFactory

class ChatHolderFactory: HolderFactory()
{
    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*> {
        return when(viewType) {

        }
    }
}