package ru.fasdev.tfs.view.ui.fragment.cardProfile

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.FragmentCardProfileBinding
import ru.fasdev.tfs.domain.model.UserStatus

class CardProfileFragment : Fragment(R.layout.fragment_card_profile) {
    companion object {
        private const val KEY_AVATAR_SRC = "avatarSrc"
        private const val KEY_FULL_NAME = "fullName"
        private const val KEY_STATUS = "status"
        private const val KEY_IS_ONLINE = "isOnline"

        private const val ONLINE_COLOR = R.color.green_500
        private const val OFFLINE_COLOR = R.color.red_500

        private const val DEFAULT_IMAGE = R.drawable.ic_launcher_background

        fun newInstance(
            avatarSrc: String,
            fullName: String,
            status: String,
            isOnline: Boolean
        ): CardProfileFragment {
            return CardProfileFragment().apply {
                arguments = bundleOf(
                    KEY_AVATAR_SRC to avatarSrc, KEY_FULL_NAME to fullName,
                    KEY_STATUS to status, KEY_IS_ONLINE to isOnline
                )
            }
        }
    }

    private var _binding: FragmentCardProfileBinding? = null
    private val binding get() = _binding!!

    var avatarSrc: String? = null
        set(value) {
            field = value
            updateAvatarSrc(value)
        }

    var fullName: String? = null
        set(value) {
            field = value
            updateFullName(value)
        }

    var status: UserStatus? = null
        set(value) {
            field = value
            updateStatusText(value)
        }

    var isOnline: Boolean = false
        set(value) {
            field = value
            updateOnlineIndicator(value)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)?.apply {
            _binding = FragmentCardProfileBinding.bind(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        avatarSrc = arguments?.getString(KEY_AVATAR_SRC)
        fullName = arguments?.getString(KEY_FULL_NAME)
        status = arguments?.getSerializable(KEY_STATUS) as UserStatus?
        isOnline = arguments?.getBoolean(KEY_IS_ONLINE) ?: false
    }

    private fun updateAvatarSrc(avatarSrc: String?) {
        avatarSrc?.let {
            // TODO: LOAD OTHER IMAGE
        } ?: kotlin.run {
            binding.avatar.setImageResource(DEFAULT_IMAGE)
        }
    }

    private fun updateFullName(fullName: String?) {
        binding.fullName.text = fullName
    }

    private fun updateStatusText(userStatus: UserStatus?) {
        userStatus?.let {
            binding.status.isGone = false

            val text = when (userStatus) {
                UserStatus.MEETING -> resources.getString(R.string.status_meeting)
                UserStatus.FREE -> resources.getString(R.string.status_free)
            }

            binding.status.text = text
        } ?: kotlin.run {
            binding.status.isGone = true
        }
    }

    private fun updateOnlineIndicator(isOnline: Boolean) {
        if (isOnline) {
            binding.onlineStatus.setTextColor(ContextCompat.getColor(requireContext(), ONLINE_COLOR))
            binding.onlineStatus.text = resources.getString(R.string.online)
        } else {
            binding.onlineStatus.setTextColor(ContextCompat.getColor(requireContext(), OFFLINE_COLOR))
            binding.onlineStatus.text = resources.getString(R.string.offline)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
