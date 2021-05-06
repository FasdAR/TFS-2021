package ru.fasdev.tfs.screen.fragment.people.recycler.viewType

import ru.fasdev.tfs.R
import ru.fasdev.tfs.domain.old.user.model.UserStatus
import ru.fasdev.tfs.recycler.viewHolder.ViewType

data class UserUi(
    override val uId: Int,
    val avatarSrc: String,
    val fullName: String,
    val email: String,
    val userStatus: UserStatus,
    override val viewType: Int = R.layout.item_user
) : ViewType()
