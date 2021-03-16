package ru.fasdev.tfs.view.ui.fragment.chat.adapter

import android.view.View
import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.feature.recycler.base.BaseViewHolder
import ru.fasdev.tfs.view.feature.recycler.base.HolderFactory
import ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewHolders.DateViewHolder
import ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewHolders.ExternalMessageViewHolder
import ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewHolders.InternalMessageViewHolder

class ChatHolderFactory: HolderFactory()
{
    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>? {
        return when(viewType) {
            R.layout.item_date_separation -> DateViewHolder(view)
            R.layout.item_external_message -> ExternalMessageViewHolder(view)
            R.layout.item_internal_message -> InternalMessageViewHolder(view)
            else -> null
        }
    }
}