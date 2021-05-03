package ru.fasdev.tfs.screen.fragment.profileAnother

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.fasdev.tfs.R
import ru.fasdev.tfs.TfsApp
import ru.fasdev.tfs.core.ext.setSystemInsetsInTop
import ru.fasdev.tfs.data.mapper.toUserUi
import ru.fasdev.tfs.databinding.FragmentAnotherProfileBinding
import ru.fasdev.tfs.di.module.UserDomainModule
import ru.fasdev.tfs.di.provide.ProvideFragmentRouter
import ru.fasdev.tfs.domain.user.interactor.UserInteractor
import ru.fasdev.tfs.domain.user.interactor.UserInteractorImpl
import ru.fasdev.tfs.fragmentRouter.FragmentRouter
import ru.fasdev.tfs.fragmentRouter.FragmentScreen
import ru.fasdev.tfs.screen.fragment.profile.ProfileViewModel
import ru.fasdev.tfs.screen.fragment.profile.mvi.ProfileAction
import ru.fasdev.tfs.screen.fragment.profileAnother.mvi.ProfileAnotherAction
import ru.fasdev.tfs.screen.fragment.profileAnother.mvi.ProfileAnotherState
import ru.fasdev.tfs.view.MviView
import ru.fasdev.tfs.view.ui.fragment.cardProfile.CardProfileFragment

class ProfileAnotherFragment : Fragment(R.layout.fragment_another_profile), MviView<ProfileAnotherState> {
    companion object {
        private val TAG: String = ProfileAnotherFragment::class.java.simpleName

        private const val NULL_USER = -1L
        private const val KEY_ID_USER = "id_user"

        fun newInstance(idUser: Long): ProfileAnotherFragment {
            return ProfileAnotherFragment().apply {
                arguments = bundleOf(KEY_ID_USER to idUser)
            }
        }

        fun getScreen(idUser: Long) = FragmentScreen(TAG, newInstance(idUser))
    }

    private var _binding: FragmentAnotherProfileBinding? = null
    private val binding get() = _binding!!

    private val rootRouter: FragmentRouter
        get() = (requireActivity() as ProvideFragmentRouter).getRouter()

    private val idUser: Long get() = arguments?.getLong(KEY_ID_USER, NULL_USER) ?: NULL_USER

    private val cardProfile
        get() = childFragmentManager.findFragmentById(R.id.card_profile) as CardProfileFragment

    private val viewModel: ProfileAnotherViewModel by viewModels()
    private var disposable: Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)?.apply {
            _binding = FragmentAnotherProfileBinding.bind(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarLayout.apply {
            root.setSystemInsetsInTop()
            title.text = resources.getString(R.string.profile)
            btnNav.isVisible = true
            btnNav.setOnClickListener { rootRouter.back() }
        }

        viewModel.attachView(this)

        viewModel.input.accept(ProfileAnotherAction.SideEffectLoadUser(idUser))
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
        _binding = null
    }

    private fun onError(error: Throwable) {
        Snackbar.make(binding.root, error.message.toString(), Snackbar.LENGTH_LONG).show()
    }

    override fun render(state: ProfileAnotherState) {
        when {
            state.error != null -> {
                onError(state.error)
            }
            state.isLoading -> {
                Log.d("LOADING", "LLOADING")
            }
            else -> {
                cardProfile.avatarSrc = state.userAvatar
                cardProfile.fullName = state.userFullName
                cardProfile.status = state.userStatus
            }
        }
    }
    // #endregion
}
