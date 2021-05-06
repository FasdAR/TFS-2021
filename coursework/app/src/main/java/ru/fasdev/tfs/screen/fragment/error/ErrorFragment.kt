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
    companion object {
        private val TAG: String = ErrorFragment::class.java.simpleName
        private const val KEY_ICON = "icon"
        private const val KEY_DESCRIPTION = "desc"
        private const val KEY_BUTTON = "btn"

        private fun newInstance(
            iconRes: Int? = null,
            descriptionText: String,
            buttonText: String? = null
        ): ErrorFragment = ErrorFragment().apply {
            arguments = bundleOf(KEY_ICON to iconRes, KEY_DESCRIPTION to descriptionText, KEY_BUTTON to buttonText)
        }

        fun getScreen(
            iconRes: Int? = null,
            descriptionText: String,
            buttonText: String? = null
        ) = FragmentScreen(TAG, newInstance(iconRes, descriptionText, buttonText))
    }

    private var _binding: FragmentErrorNetworkBinding? = null
    private val binding get() = _binding!!

    private val iconRes get() = arguments?.getInt(KEY_ICON)
    private val descriptionText get() = arguments?.getString(KEY_DESCRIPTION)
    private val buttonText get() = arguments?.getString(KEY_BUTTON)

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

        iconRes?.let {
            binding.icon.setImageResource(it)
        } ?: kotlin.run {
            binding.icon.isGone = false
        }

        descriptionText?.let {
            binding.text.text = it
        } ?: kotlin.run {
            binding.text.isGone = false
        }

        //TODO: ADD BUTTON
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}