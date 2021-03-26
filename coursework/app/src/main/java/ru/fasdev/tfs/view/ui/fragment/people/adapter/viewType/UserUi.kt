package ru.fasdev.tfs.view.ui.fragment.people.adapter.viewType

import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.ui.global.recycler.base.ViewType

data class UserUi(
    override val uId: Int,
    val avatarSrc: String,
    val fullName: String,
    val email: String,
    val isOnline: Boolean,
    override val viewType: Int = R.layout.item_user
) : ViewType()
