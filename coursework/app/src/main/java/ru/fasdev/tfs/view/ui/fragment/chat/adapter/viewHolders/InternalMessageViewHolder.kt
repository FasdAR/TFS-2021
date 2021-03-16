package ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewHolders

import android.view.View
import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.feature.customView.viewGroup.message.InternalMessageViewGroup
import ru.fasdev.tfs.view.feature.recycler.base.BaseViewHolder
import ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewTypes.InternalMessageUi

class InternalMessageViewHolder(view: View): BaseViewHolder<InternalMessageUi>(view)
{
    val message: InternalMessageViewGroup = view.findViewById(R.id.message)

    override fun bind(item: InternalMessageUi) {
        message.message = item.message
        message.reactions = item.reactions
    }
}