package ru.fasdev.tfs.screen.fragment.ownProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.jakewharton.rxrelay2.PublishRelay
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.FragmentOwnProfileBinding
import ru.fasdev.tfs.domain.old.user.model.UserStatus
import ru.fasdev.tfs.fragmentRouter.FragmentScreen
import ru.fasdev.tfs.mviCore.MviView
import ru.fasdev.tfs.mviCore.entity.action.Action
import ru.fasdev.tfs.screen.fragment.cardProfile.CardProfileFragment
import ru.fasdev.tfs.screen.fragment.error.ErrorFragment
import ru.fasdev.tfs.screen.fragment.ownProfile.mvi.OwnProfileAction
import ru.fasdev.tfs.screen.fragment.ownProfile.mvi.OwnProfileState
import java.net.UnknownHostException

class OwnProfileFragment : Fragment(R.layout.fragment_own_profile), MviView<Action, OwnProfileState>, ErrorFragment.Listener {
    companion object {
        private val TAG: String = OwnProfileFragment::class.java.simpleName
        private fun newInstance(): OwnProfileFragment = OwnProfileFragment()
        fun getScreen() = FragmentScreen(TAG, newInstance())
    }

    override val actions: PublishRelay<Action> = PublishRelay.create()
    private val viewModel: OwnProfileViewModel by viewModels()

    private var _binding: FragmentOwnProfileBinding? = null
    private val binding get() = _binding!!

    private val errorFragment get() = childFragmentManager.findFragmentById(R.id.error_placeholder) as ErrorFragment
    private val cardProfileFragment get() = childFragmentManager.findFragmentById(R.id.card_profile) as CardProfileFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)?.apply {
            _binding = FragmentOwnProfileBinding.bind(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.bind(this)
    }

    override fun render(state: OwnProfileState) {
        if (state.error != null) {
            binding.cardProfile.isGone = true
            binding.errorPlaceholder.isGone = false

            when (state.error) {
                is UnknownHostException -> {
                    errorFragment.iconRes = R.drawable.ic_cloud_off
                    errorFragment.descriptionText = resources.getString(R.string.check_network_connection)
                    errorFragment.positiveBtnText = resources.getString(R.string.try_again)
                }
                else -> {
                    errorFragment.iconRes = R.drawable.ic_error
                    errorFragment.descriptionText = resources.getString(R.string.error_occurred, state.error.message.toString())
                    errorFragment.positiveBtnText = resources.getString(R.string.try_again)
                }
            }
        }
        else {
            binding.errorPlaceholder.isGone = true
            binding.cardProfile.isGone = false

            if (state.isLoading) {
                cardProfileFragment.startShimmer()
            }
            else {
                cardProfileFragment.stopShimmer()

                cardProfileFragment.apply {
                    avatarSrc = state.user?.avatarUrl
                    fullName = state.user?.fullName
                    status = UserStatus.ONLINE // TODO: GET IN NETWORK
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        viewModel.unBind()
    }

    override fun onClickPositiveBtn() {
        actions.accept(OwnProfileAction.Ui.LoadUser)
    }
}
