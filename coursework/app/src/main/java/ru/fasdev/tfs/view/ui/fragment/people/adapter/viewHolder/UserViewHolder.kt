package ru.fasdev.tfs.view.ui.fragment.people.adapter.viewHolder

import android.view.View
import androidx.core.text.isDigitsOnly
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.ItemUserBinding
import ru.fasdev.tfs.view.ui.fragment.people.adapter.viewType.UserUi
import ru.fasdev.tfs.view.ui.global.recycler.base.BaseViewHolder

class UserViewHolder(val view: View) : BaseViewHolder<UserUi>(view) {
    private val binding = ItemUserBinding.bind(view)

    override fun bind(item: UserUi) {
        binding.fullName.text = item.fullName
        binding.email.text = item.email

        var avatarRes = item.avatarSrc
        if (avatarRes.isEmpty()) avatarRes = R.drawable.ic_launcher_background.toString()

        if (avatarRes.isDigitsOnly()) {
            binding.avatar.setImageResource(avatarRes.toInt())
        }
    }
}