package ru.fasdev.tfs.screen.fragment.people.recycler.viewHolder

import android.view.View
import androidx.core.text.isDigitsOnly
import coil.load
import coil.transform.CircleCropTransformation
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.ItemUserBinding
import ru.fasdev.tfs.domain.user.model.UserStatus.IDLE
import ru.fasdev.tfs.domain.user.model.UserStatus.OFFLINE
import ru.fasdev.tfs.domain.user.model.UserStatus.ONLINE
import ru.fasdev.tfs.recycler.viewHolder.ViewHolder
import ru.fasdev.tfs.screen.fragment.people.recycler.viewType.UserUi

class UserViewHolder(val view: View, private val clickUser: OnClickUserListener) : ViewHolder<UserUi>(view) {
    fun interface OnClickUserListener {
        fun onClickUser(idUser: Int, email: String)
    }

    private val binding = ItemUserBinding.bind(view)

    private var item: UserUi? = null

    init {
        binding.root.setOnClickListener { clickUser.onClickUser(item?.uId ?: -1, item?.email.toString()) }
    }

    override fun bind(item: UserUi) {
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
            ONLINE -> R.drawable.sh_indicator_online
            OFFLINE -> R.drawable.sh_indicator_offline
            IDLE -> R.drawable.sh_indicator_idle
        }

        binding.onlineStatus.setImageResource(indicatorOnline)
    }
}
