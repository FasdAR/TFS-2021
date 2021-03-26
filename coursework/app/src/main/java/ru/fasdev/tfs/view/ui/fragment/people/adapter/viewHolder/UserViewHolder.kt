package ru.fasdev.tfs.view.ui.fragment.people.adapter.viewHolder

import android.view.View
import androidx.core.text.isDigitsOnly
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.ItemUserBinding
import ru.fasdev.tfs.view.ui.fragment.people.adapter.viewType.UserUi
import ru.fasdev.tfs.view.ui.global.recycler.base.BaseViewHolder

class UserViewHolder(val view: View, private val clickUser: OnClickUserListener) : BaseViewHolder<UserUi>(view) {
    fun interface OnClickUserListener {
        fun onClickUser(idUser: Int)
    }

    private val binding = ItemUserBinding.bind(view)

    private var item: UserUi? = null

    init {
        binding.root.setOnClickListener { clickUser.onClickUser(item?.uId ?: -1) }
    }

    override fun bind(item: UserUi) {
        this.item = item

        binding.fullName.text = item.fullName
        binding.email.text = item.email

        var avatarRes = item.avatarSrc
        if (avatarRes.isEmpty()) avatarRes = R.drawable.ic_launcher_background.toString()
        if (avatarRes.isDigitsOnly()) binding.avatar.setImageResource(avatarRes.toInt())

        if (item.isOnline) binding.onlineStatus.setImageResource(R.drawable.sh_indicator_online)
        else binding.onlineStatus.setImageResource(R.drawable.sh_indicator_offline)
    }
}
