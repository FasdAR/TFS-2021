package ru.fasdev.tfs.screen.fragment.error

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.FragmentErrorNetworkBinding
import ru.fasdev.tfs.fragmentRouter.FragmentScreen

class ErrorFragment: Fragment(R.layout.fragment_error_network)
{
    fun interface Listener {
        fun onClickPositiveBtn()
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
                (parentFragment as Listener).onClickPositiveBtn()
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