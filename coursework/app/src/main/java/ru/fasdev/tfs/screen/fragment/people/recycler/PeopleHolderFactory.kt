package ru.fasdev.tfs.screen.fragment.people.recycler

import android.view.View
import ru.fasdev.tfs.R
import ru.fasdev.tfs.recycler.base.viewHolder.ViewHolder
import ru.fasdev.tfs.recycler.base.viewHolder.ViewHolderFactory
import ru.fasdev.tfs.recycler.item.user.UserViewHolder

class PeopleHolderFactory(private val onClickUserListener: UserViewHolder.OnClickUserListener) :
    ViewHolderFactory() {
    override fun createViewHolder(view: View, viewType: Int): ViewHolder<*>? {
        return when (viewType) {
            R.layout.item_user -> UserViewHolder(view, onClickUserListener)
            else -> null
        }
    }
}
