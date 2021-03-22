package ru.fasdev.tfs.view.ui.fragment.people.adapter

import android.view.View
import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.ui.fragment.people.adapter.viewHolder.UserViewHolder
import ru.fasdev.tfs.view.ui.global.recycler.base.BaseViewHolder
import ru.fasdev.tfs.view.ui.global.recycler.base.HolderFactory

class PeopleHolderFactory(val onClickUserListener: UserViewHolder.OnClickUserListener) : HolderFactory()
{
    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>? {
        return when (viewType) {
            R.layout.item_user -> UserViewHolder(view, onClickUserListener)
            else -> null
        }
    }
}