package ru.fasdev.tfs.recycler.item.user

import android.view.View
import androidx.core.text.isDigitsOnly
import coil.load
import coil.transform.CircleCropTransformation
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.ItemUserBinding
import ru.fasdev.tfs.domain.user.model.UserOnlineStatus
import ru.fasdev.tfs.recycler.base.viewHolder.ViewHolder

class UserViewHolder(val view: View, private val clickUser: OnClickUserListener) : ViewHolder<UserItem>(view) {
    fun interface OnClickUserListener {
        fun onClickUser(idUser: Int, email: String)
    }

    private val binding = ItemUserBinding.bind(view)

    private var item: UserItem? = null

    init {
        binding.root.setOnClickListener { clickUser.onClickUser(item?.uId ?: -1, item?.email.toString()) }
    }

    override fun bind(item: UserItem) {
        this.item = item

        binding.fullName.text = item.fullName
        binding.email.text = item.email

        var avatarRes = item.avatarSrc
        if (avatarRes.isEmpty()) avatarRes = R.drawable.ic_launcher_background.toString()
        if (avatarRes.isDigitsOnly()) binding.avatar.setImageResource(avatarRes.toInt())
        else binding.avatar.load(avatarRes) {
            crossfade(true)
            transformations(CircleCropTransformation())
        }

        val indicatorOnline = when (item.userStatus) {
            UserOnlineStatus.ONLINE -> R.drawable.sh_indicator_online
            UserOnlineStatus.OFFLINE -> R.drawable.sh_indicator_offline
            UserOnlineStatus.IDLE -> R.drawable.sh_indicator_idle
        }

        binding.onlineStatus.setImageResource(indicatorOnline)
    }
}
