package ru.fasdev.tfs.screen.fragment.people.recycler

import android.view.View
import ru.fasdev.tfs.R
import ru.fasdev.tfs.screen.fragment.people.recycler.viewHolder.UserViewHolder
import ru.fasdev.tfs.recycler.viewHolder.ViewHolder
import ru.fasdev.tfs.recycler.viewHolder.ViewHolderFactory

class PeopleHolderFactory(private val onClickUserListener: UserViewHolder.OnClickUserListener) :
    ViewHolderFactory() {
    override fun createViewHolder(view: View, viewType: Int): ViewHolder<*>? {
        return when (viewType) {
            R.layout.item_user -> UserViewHolder(view, onClickUserListener)
            else -> null
        }
    }
}
