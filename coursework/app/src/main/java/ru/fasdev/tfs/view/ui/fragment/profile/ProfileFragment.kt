package ru.fasdev.tfs.view.ui.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.FragmentProfileBinding
import ru.fasdev.tfs.domain.model.UserStatus
import ru.fasdev.tfs.view.feature.util.getCurrentFragment
import ru.fasdev.tfs.view.ui.fragment.cardProfile.CardProfileFragment
import ru.fasdev.tfs.view.ui.global.fragmentRouter.FragmentScreen

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    companion object {
        private val TAG: String = ProfileFragment::class.java.simpleName
        fun newInstance(): ProfileFragment = ProfileFragment()
        fun getScreen() = FragmentScreen(TAG, newInstance())
    }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        childFragmentManager.addFragmentOnAttachListener { fragmentManager, fragment ->
            val cardProfile = fragment as CardProfileFragment
            cardProfile.lifecycle.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
                fun onResume() {
                    cardProfile.isOnline = true
                    cardProfile.status = UserStatus.MEETING
                    cardProfile.fullName = "Test User"
                }
            })
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)?.apply {
            _binding = FragmentProfileBinding.bind(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logoutBtn.setOnClickListener {
            // TODO: ADD logout
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
