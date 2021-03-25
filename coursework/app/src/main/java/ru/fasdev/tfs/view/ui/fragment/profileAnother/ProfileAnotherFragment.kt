package ru.fasdev.tfs.view.ui.fragment.profileAnother

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.FragmentAnotherProfileBinding
import ru.fasdev.tfs.domain.user.interactor.UserInteractor
import ru.fasdev.tfs.domain.user.interactor.UserInteractorImpl
import ru.fasdev.tfs.domain.user.repo.TestUserRepoImpl
import ru.fasdev.tfs.view.ui.fragment.cardProfile.CardProfileFragment
import ru.fasdev.tfs.view.ui.global.fragmentRouter.FragmentRouter
import ru.fasdev.tfs.view.ui.global.fragmentRouter.FragmentScreen

class ProfileAnotherFragment : Fragment(R.layout.fragment_another_profile)
{
    companion object {
        val TAG: String = ProfileAnotherFragment::class.java.simpleName

        private const val NULL_USER = -1
        private const val KEY_ID_USER = "id_user"

        fun newInstance(idUser: Int): ProfileAnotherFragment = ProfileAnotherFragment().apply {
            arguments = bundleOf(KEY_ID_USER to idUser)
        }

        fun getScreen(idUser: Int) = FragmentScreen(TAG, newInstance(idUser))
    }

    private var _binding: FragmentAnotherProfileBinding? = null
    private val binding get() = _binding!!

    private val rootRouter: FragmentRouter
        get() = requireActivity() as FragmentRouter

    private val testUserRepo = TestUserRepoImpl()
    private val userInteractor: UserInteractor = UserInteractorImpl(testUserRepo)

    private val idUser: Int get() = arguments?.getInt(KEY_ID_USER, NULL_USER) ?: NULL_USER

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        view?.let { _binding = FragmentAnotherProfileBinding.bind(it) }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userInteractor.getUserById(idUser)?.let { user ->
            val cardProfile = childFragmentManager.findFragmentById(R.id.card_profile) as CardProfileFragment
            cardProfile.fullName = user.fullName
            cardProfile.status = userInteractor.getStatusUser(user.id)
            cardProfile.isOnline = userInteractor.isOnlineUser(user.id)
        }

        binding.toolbar.setNavigationOnClickListener {
            rootRouter.back()
        }
        binding.toolbar.title
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}