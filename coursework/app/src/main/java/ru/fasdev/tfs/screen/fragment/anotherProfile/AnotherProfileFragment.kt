package ru.fasdev.tfs.screen.fragment.anotherProfile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.jakewharton.rxrelay2.PublishRelay
import ru.fasdev.tfs.R
import ru.fasdev.tfs.core.ext.setSystemInsetsInTop
import ru.fasdev.tfs.databinding.FragmentAnotherProfileBinding
import ru.fasdev.tfs.di.provide.ProvideFragmentRouter
import ru.fasdev.tfs.fragmentRouter.FragmentRouter
import ru.fasdev.tfs.fragmentRouter.FragmentScreen
import ru.fasdev.tfs.mviCore.MviView
import ru.fasdev.tfs.mviCore.entity.action.Action
import ru.fasdev.tfs.screen.fragment.anotherProfile.mvi.AnotherProfileAction
import ru.fasdev.tfs.screen.fragment.anotherProfile.mvi.AnotherProfileState
import ru.fasdev.tfs.screen.fragment.cardProfile.CardProfileFragment
import ru.fasdev.tfs.screen.fragment.info.InfoPlaceholderFragment
import ru.fasdev.tfs.screen.fragment.info.handleErrorState

class AnotherProfileFragment : Fragment(R.layout.fragment_another_profile),
    MviView<Action, AnotherProfileState>, InfoPlaceholderFragment.Listener {
    companion object {
        private val TAG: String = AnotherProfileFragment::class.java.simpleName

        private const val KEY_ID_USER = "id_user"
        private const val NULL_USER = -1L

        private fun newInstance(idUser: Long): AnotherProfileFragment {
            return AnotherProfileFragment().apply {
                arguments = bundleOf(KEY_ID_USER to idUser)
            }
        }

        fun getScreen(idUser: Long) = FragmentScreen(TAG, newInstance(idUser))
    }

    override val actions: PublishRelay<Action> = PublishRelay.create()
    private val viewModel: AnotherProfileViewModel by viewModels()

    private var _binding: FragmentAnotherProfileBinding? = null
    private val binding get() = _binding!!

    private val rootRouter: FragmentRouter
        get() = (requireActivity() as ProvideFragmentRouter).getRouter()

    private val idUser: Long
        get() = arguments?.getLong(KEY_ID_USER) ?: NULL_USER

    private val infoFragment
        get() = childFragmentManager.findFragmentById(R.id.info_placeholder) as InfoPlaceholderFragment
    private val cardProfileFragment
        get() = childFragmentManager.findFragmentById(R.id.card_profile) as CardProfileFragment

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

        viewModel.bind(this)
        actions.accept(AnotherProfileAction.Ui.LoadUser(idUser))
    }

    override fun render(state: AnotherProfileState) {
        Log.d("STATE_RENDER", state.toString())
        if (state.error != null) {
            binding.cardProfile.isGone = true
            binding.infoPlaceholder.isGone = false

            infoFragment.handleErrorState(state.error)
        }
        else {
            binding.infoPlaceholder.isGone = true
            binding.cardProfile.isGone = false

            if (state.isLoading) {
                cardProfileFragment.startShimmer()
            }
            else {
                cardProfileFragment.stopShimmer()

                cardProfileFragment.apply {
                    avatarSrc = state.user?.avatarUrl
                    fullName = state.user?.fullName
                    status = state.user?.onlineStatus
                }
            }
        }
    }

    override fun onBtnClickInfoPlaceholder(buttonType: InfoPlaceholderFragment.ButtonType) {
        when (buttonType) {
            InfoPlaceholderFragment.ButtonType.POSITIVE -> {
                actions.accept(AnotherProfileAction.Ui.LoadUser(idUser))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        viewModel.unBind()
    }
}
