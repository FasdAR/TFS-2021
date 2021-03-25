package ru.fasdev.tfs.view.ui.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.FragmentProfileBinding
import ru.fasdev.tfs.domain.model.UserStatus
import ru.fasdev.tfs.view.ui.fragment.cardProfile.CardProfileFragment

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

        val cardProfile = childFragmentManager.findFragmentById(R.id.card_profile) as CardProfileFragment
        cardProfile.isOnline = true
        cardProfile.status = UserStatus.MEETING
        cardProfile.fullName = "Test User"

        binding.logoutBtn.setOnClickListener {
            //TODO: ADD logout
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}