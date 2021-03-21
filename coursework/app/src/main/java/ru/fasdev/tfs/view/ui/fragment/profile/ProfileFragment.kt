package ru.fasdev.tfs.view.ui.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.FragmentProfileBinding

class ProfileFragment : Fragment(R.layout.fragment_profile)
{
    companion object {
        val TAG: String = ProfileFragment::class.java.simpleName
        fun newInstance(): ProfileFragment = ProfileFragment()
    }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        view?.let { _binding = FragmentProfileBinding.bind(it) }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.layoutInfo.avatar.setImageResource(R.drawable.ic_launcher_background)
        binding.layoutInfo.fullName.text = "Andrey Rednikov"
        binding.layoutInfo.status.text = resources.getString(R.string.status_meeting)
        binding.layoutInfo.onlineStatus.text = resources.getString(R.string.online)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}