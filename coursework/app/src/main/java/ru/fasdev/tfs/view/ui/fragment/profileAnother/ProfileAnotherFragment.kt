package ru.fasdev.tfs.view.ui.fragment.profileAnother

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.FragmentAnotherProfileBinding
import ru.fasdev.tfs.domain.model.User
import ru.fasdev.tfs.domain.user.interactor.UserInteractor
import ru.fasdev.tfs.domain.user.interactor.UserInteractorImpl
import ru.fasdev.tfs.domain.user.repo.TestUserRepoImpl
import ru.fasdev.tfs.view.ui.global.fragmentRouter.FragmentRouter

class ProfileAnotherFragment : Fragment(R.layout.fragment_another_profile)
{
    companion object {
        val TAG: String = ProfileAnotherFragment::class.java.simpleName

        private const val NULL_USER = -1
        private const val KEY_ID_USER = "id_user"

        fun newInstance(idUser: Int): ProfileAnotherFragment = ProfileAnotherFragment().apply {
            arguments = bundleOf(KEY_ID_USER to idUser)
        }
    }

    private var _binding: FragmentAnotherProfileBinding? = null
    private val binding get() = _binding!!

    private val rootRouter: FragmentRouter
        get() = requireActivity() as FragmentRouter

    private val testUserRepo = TestUserRepoImpl()
    private val userInteractor: UserInteractor = UserInteractorImpl(testUserRepo)

    private val idUser: Int get() = arguments?.getInt(KEY_ID_USER,NULL_USER) ?: NULL_USER

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        view?.let { _binding = FragmentAnotherProfileBinding.bind(it) }
        return view
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userInteractor.getUserById(idUser)?.let { user ->
            binding.layoutInfo.avatar.setImageResource(R.drawable.ic_launcher_background)
            binding.layoutInfo.fullName.text = user.fullName

            val status = when (userInteractor.getStatusUser(user.id)) {
                User.USER_IS_FREE -> {
                    resources.getString(R.string.status_meeting)
                }
                User.USER_IN_MEETING -> {
                    resources.getString(R.string.status_free)
                }
                else -> ""
            }

            val isOnline = userInteractor.isOnlineUser(user.id)
            val onlineStatus = if (isOnline) resources.getString(R.string.online)
                else resources.getString(R.string.offline)

            if (isOnline)
                binding.layoutInfo.onlineStatus.setTextColor(getColor(requireContext(), R.color.green_500))
            else
                binding.layoutInfo.onlineStatus.setTextColor(getColor(requireContext(), R.color.red_500))

            binding.layoutInfo.status.text = status
            binding.layoutInfo.onlineStatus.text = onlineStatus
        }

        binding.toolbar.setNavigationIcon(R.drawable.ic_back)
        binding.toolbar.setNavigationOnClickListener {
            rootRouter.back()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}