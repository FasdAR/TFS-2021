package ru.fasdev.tfs.screen.fragment.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import ru.fasdev.tfs.R
import ru.fasdev.tfs.TfsApp
import ru.fasdev.tfs.data.mapper.toUserUi
import ru.fasdev.tfs.databinding.FragmentProfileBinding
import ru.fasdev.tfs.di.module.UserDomainModule
import ru.fasdev.tfs.domain.user.interactor.UserInteractorImpl
import ru.fasdev.tfs.fragmentRouter.FragmentScreen
import ru.fasdev.tfs.screen.fragment.cardProfile.CardProfileFragment
import ru.fasdev.tfs.screen.fragment.profile.mvi.ProfileAction
import ru.fasdev.tfs.screen.fragment.profile.mvi.ProfileState
import ru.fasdev.tfs.view.MviView

class ProfileFragment : Fragment(R.layout.fragment_profile), MviView<ProfileState> {
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

        viewModel.attachView(this)

        viewModel.input.accept(ProfileAction.SideEffectLoadUser)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun render(state: ProfileState) {
        when {
            state.error != null -> {
                Log.e("ERROE", state.error.message.toString())
            }
            state.isLoading -> {
                Log.d("LOADING", "LLOADING")
            }
            else -> {
                val cardProfile = childFragmentManager.findFragmentById(R.id.card_profile) as CardProfileFragment
                cardProfile.avatarSrc = state.userAvatar
                cardProfile.fullName = state.userFullName
                cardProfile.status = state.userStatus
            }
        }
    }
}
