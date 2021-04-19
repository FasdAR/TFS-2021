package ru.fasdev.tfs.screen.fragment.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.fasdev.tfs.R
import ru.fasdev.tfs.TfsApp
import ru.fasdev.tfs.data.mapper.toUserUi
import ru.fasdev.tfs.databinding.FragmentProfileBinding
import ru.fasdev.tfs.di.module.UserDomainModule
import ru.fasdev.tfs.domain.user.interactor.UserInteractorImpl
import ru.fasdev.tfs.fragmentRouter.FragmentScreen
import ru.fasdev.tfs.screen.fragment.cardProfile.CardProfileFragment

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    companion object {
        val TAG: String = ProfileFragment::class.java.simpleName
        fun newInstance(): ProfileFragment = ProfileFragment()
        fun getScreen() = FragmentScreen(TAG, newInstance())
    }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)?.apply {
            _binding = FragmentProfileBinding.bind(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cardProfile = childFragmentManager.findFragmentById(R.id.card_profile) as CardProfileFragment
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
