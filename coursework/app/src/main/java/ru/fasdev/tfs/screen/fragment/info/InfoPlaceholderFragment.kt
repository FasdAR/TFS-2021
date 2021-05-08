package ru.fasdev.tfs.screen.fragment.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.FragmentErrorNetworkBinding
import java.net.UnknownHostException

fun InfoPlaceholderFragment.handleErrorState(error: Throwable) {
    when (error) {
        is UnknownHostException -> {
            networkErrorState()
        }
        else -> {
            otherErrorState(error.message.toString())
        }
    }
}

fun InfoPlaceholderFragment.networkErrorState() {
    iconRes = R.drawable.ic_cloud_off
    descriptionText = resources.getString(R.string.check_network_connection)
    positiveBtnText = resources.getString(R.string.try_again)
}

fun InfoPlaceholderFragment.otherErrorState(error: String) {
    iconRes = R.drawable.ic_error
    descriptionText = resources.getString(R.string.error_occurred, error.trim().replace(".", ""))
    positiveBtnText = resources.getString(R.string.try_again)
}

fun InfoPlaceholderFragment.emptyListState(description: String) {
    iconRes = R.drawable.ic_list
    descriptionText = description
    positiveBtnText = resources.getString(R.string.update)
}

class InfoPlaceholderFragment: Fragment(R.layout.fragment_error_network)
{
    enum class ButtonType {
        POSITIVE
    }

    fun interface Listener {
        fun onBtnClickInfoPlaceholder(buttonType: ButtonType)
    }

    private var _binding: FragmentErrorNetworkBinding? = null
    private val binding get() = _binding!!

    var iconRes: Int? = null
        set(value) {
            field = value
            updateIcon(value)
        }

    var descriptionText: String? = null
        set(value) {
            field = value
            updateDescription(value)
        }

    var positiveBtnText: String? = null
        set(value) {
            field = value
            updatePositiveBtn(value)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)?.apply {
            _binding = FragmentErrorNetworkBinding.bind(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.positiveBtn.setOnClickListener {
            if (parentFragment is Listener) {
                (parentFragment as Listener).onBtnClickInfoPlaceholder(ButtonType.POSITIVE)
            }
        }
    }

    private fun updateIcon(iconRes: Int?) {
        iconRes?.let {
            binding.icon.isGone = false
            binding.icon.setImageResource(it)
        } ?: kotlin.run {
            binding.icon.isGone = true
        }
    }

    private fun updateDescription(descriptionText: String?) {
        descriptionText?.let {
            binding.icon.isGone = false
            binding.text.text = it
        } ?: kotlin.run {
            binding.text.isGone = true
        }
    }

    private fun updatePositiveBtn(btnText: String?) {
        btnText?.let {
            binding.icon.isGone = false
            binding.positiveBtn.text = it
        } ?: kotlin.run {
            binding.positiveBtn.isGone = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}