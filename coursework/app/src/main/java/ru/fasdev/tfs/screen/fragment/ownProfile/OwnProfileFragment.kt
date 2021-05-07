package ru.fasdev.tfs.screen.fragment.ownProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxrelay2.PublishRelay
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.FragmentOwnProfileBinding
import ru.fasdev.tfs.domain.old.user.model.UserStatus
import ru.fasdev.tfs.fragmentRouter.FragmentScreen
import ru.fasdev.tfs.mviCore.MviView
import ru.fasdev.tfs.mviCore.entity.action.Action
import ru.fasdev.tfs.screen.fragment.cardProfile.CardProfileFragment
import ru.fasdev.tfs.screen.fragment.ownProfile.mvi.OwnProfileAction
import ru.fasdev.tfs.screen.fragment.ownProfile.mvi.OwnProfileState

class OwnProfileFragment : Fragment(R.layout.fragment_own_profile), MviView<Action, OwnProfileState> {
    companion object {
        private val TAG: String = OwnProfileFragment::class.java.simpleName
        private fun newInstance(): OwnProfileFragment = OwnProfileFragment()
        fun getScreen() = FragmentScreen(TAG, newInstance())
    }

    override val actions: PublishRelay<Action> = PublishRelay.create()
    private val viewModel: OwnProfileViewModel by viewModels()

    private var _binding: FragmentOwnProfileBinding? = null
    private val binding get() = _binding!!

    private val cardProfile get() = childFragmentManager.findFragmentById(R.id.card_profile) as CardProfileFragment

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
        if (state.isLoading) {
            cardProfile.startShimmer()
        } else {
            cardProfile.stopShimmer()

            if (state.error != null) {
                Snackbar.make(binding.root, state.error.message.toString(), Snackbar.LENGTH_LONG).show()
            }
            else {
                if (state.user != null) {
                    cardProfile.avatarSrc = state.user.avatarUrl
                    cardProfile.fullName = state.user.fullName
                    cardProfile.status = UserStatus.ONLINE
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        viewModel.unBind()
    }
}
