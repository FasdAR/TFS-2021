package ru.fasdev.tfs.screen.fragment.cardProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import coil.load
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.FragmentCardProfileBinding
import ru.fasdev.tfs.domain.user.model.UserOnlineStatus

class CardProfileFragment : Fragment(R.layout.fragment_card_profile) {
    companion object {
        private const val ONLINE_COLOR = R.color.green_500
        private const val OFFLINE_COLOR = R.color.red_500
        private const val IDLE_COLOR = R.color.yellow_200

        private const val DEFAULT_IMAGE = R.drawable.ic_launcher_background
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

    var status: UserOnlineStatus? = null
        set(value) {
            field = value
            updateStatusText(value)
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

    fun startShimmer() {
        binding.dataLayer.isGone = true
        binding.shimmerLayer.isGone = false
        binding.shimmerLayer.startShimmer()
    }

    fun stopShimmer() {
        binding.shimmerLayer.stopShimmer()
        binding.dataLayer.isGone = false
        binding.shimmerLayer.isGone = true
    }

    private fun updateAvatarSrc(avatarSrc: String?) {
        avatarSrc?.let {
            binding.avatar.load(it) {
                crossfade(true)
            }
        } ?: binding.avatar.setImageResource(DEFAULT_IMAGE)
    }

    private fun updateFullName(fullName: String?) {
        binding.fullName.text = fullName
    }

    private fun updateStatusText(userStatus: UserOnlineStatus?) {
        when (userStatus) {
            UserOnlineStatus.ONLINE -> {
                binding.onlineStatus.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        ONLINE_COLOR
                    )
                )
                binding.onlineStatus.text = resources.getString(R.string.online)
            }
            UserOnlineStatus.OFFLINE -> {
                binding.onlineStatus.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        OFFLINE_COLOR
                    )
                )
                binding.onlineStatus.text = resources.getString(R.string.offline)
            }
            UserOnlineStatus.IDLE -> {
                binding.onlineStatus.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        IDLE_COLOR
                    )
                )
                binding.onlineStatus.text = resources.getString(R.string.idle)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
