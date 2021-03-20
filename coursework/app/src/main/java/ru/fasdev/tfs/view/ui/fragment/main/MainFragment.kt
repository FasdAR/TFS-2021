package ru.fasdev.tfs.view.ui.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.FragmentMainBinding
import ru.fasdev.tfs.view.feature.util.getCurrentFragment
import ru.fasdev.tfs.view.feature.util.replaceCommit
import ru.fasdev.tfs.view.ui.fragment.channels.ChannelsFragment
import ru.fasdev.tfs.view.ui.fragment.people.PeopleFragment
import ru.fasdev.tfs.view.ui.fragment.profile.ProfileFragment

class MainFragment : Fragment(R.layout.fragment_main)
{
    companion object {
        fun newInstance(): MainFragment = MainFragment()
    }

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        view?.let { _binding = FragmentMainBinding.bind(it) }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (childFragmentManager.getCurrentFragment(R.id.child_container) == null) {
            updateFragment(binding.bottomNav.selectedItemId)
        }

        binding.bottomNav.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                binding.bottomNav.selectedItemId -> false
                R.id.action_channels, R.id.action_people, R.id.action_profile -> {
                    updateFragment(item.itemId)
                    true
                }
                else -> false
            }
        }
    }

    private fun updateFragment(actionId: Int) {
        when (actionId) {
            R.id.action_channels -> {
                childFragmentManager.replaceCommit(
                        R.id.child_container,
                        ChannelsFragment.newInstance(),
                        ChannelsFragment.TAG
                )
            }
            R.id.action_people -> {
                childFragmentManager.replaceCommit(
                        R.id.child_container,
                        PeopleFragment.newInstance(),
                        PeopleFragment.TAG
                )
            }
            R.id.action_profile -> {
                childFragmentManager.replaceCommit(
                        R.id.child_container,
                        ProfileFragment.newInstance(),
                        ProfileFragment.TAG
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}