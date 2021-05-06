package ru.fasdev.tfs.screen.fragment.ownProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.FragmentOwnProfileBinding
import ru.fasdev.tfs.domain.old.user.model.UserStatus
import ru.fasdev.tfs.fragmentRouter.FragmentScreen
import ru.fasdev.tfs.screen.fragment.cardProfile.CardProfileFragment

class OwnProfileFragment : Fragment(R.layout.fragment_own_profile) {
    companion object {
        private val TAG: String = OwnProfileFragment::class.java.simpleName
        private fun newInstance(): OwnProfileFragment = OwnProfileFragment()
        fun getScreen() = FragmentScreen(TAG, newInstance())
    }

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

        viewModel.errorState.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
        }

        viewModel.networkErrorState.observe(viewLifecycleOwner) {

        }

        viewModel.isLoadingState.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                cardProfile.startShimmer()
            }
            else {
                cardProfile.stopShimmer()
            }
        }

        viewModel.userState.observe(viewLifecycleOwner) { user ->
            cardProfile.avatarSrc = user?.avatarUrl
            cardProfile.fullName = user?.fullName
            cardProfile.status = UserStatus.ONLINE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
