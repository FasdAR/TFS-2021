package ru.fasdev.tfs.recycler.item.user

import ru.fasdev.tfs.R
import ru.fasdev.tfs.domain.user.model.UserOnlineStatus
import ru.fasdev.tfs.recycler.base.viewHolder.ViewType

data class UserItem(
    override val uId: Int,
    val avatarSrc: String,
    val fullName: String,
    val email: String?,
    val userStatus: UserOnlineStatus,
    override val viewType: Int = R.layout.item_user
) : ViewType()
