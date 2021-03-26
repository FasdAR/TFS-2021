package ru.fasdev.tfs.view.ui.fragment.channels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.google.android.material.tabs.TabLayoutMediator
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.FragmentChannelsBinding
import ru.fasdev.tfs.view.feature.util.setSystemInsetsInTop
import ru.fasdev.tfs.view.ui.fragment.channels.viewPage.TopicFragmentFactory
import ru.fasdev.tfs.view.ui.global.fragmentRouter.FragmentScreen
import ru.fasdev.tfs.view.ui.global.fragmentRouter.ImplBackPressed
import ru.fasdev.tfs.view.ui.global.view.viewGroup.toolbar.SearchToolbar
import ru.fasdev.tfs.view.ui.global.viewPager.base.ViewPagerFragmentAdapter

class ChannelsFragment : Fragment(R.layout.fragment_channels), ImplBackPressed {
    companion object {
        val TAG: String = ChannelsFragment::class.java.simpleName
        fun newInstance(): ChannelsFragment = ChannelsFragment()
        fun getScreen() = FragmentScreen(TAG, newInstance())
    }

    private var _binding: FragmentChannelsBinding? = null
    private val binding get() = _binding!!

    private val vpFactoryFragment = TopicFragmentFactory()
    private lateinit var vpAdapter: ViewPagerFragmentAdapter

    val provideSearch: MutableLiveData<String> = MutableLiveData<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)?.apply {
            _binding = FragmentChannelsBinding.bind(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.toolbarLayout) {
            root.setSystemInsetsInTop()
            title.text = resources.getString(R.string.channels)
            btnSearch.isVisible = true

            btnSearch.setOnClickListener {
                binding.searchLayout.openSearch()
            }
        }

        with(binding.searchLayout) {
            setSystemInsetsInTop()

            attachToolbar = binding.toolbarLayout.root
            textChangeListener = SearchToolbar.TextChangeListener { query ->
                provideSearch.postValue(query)
            }
        }

        vpAdapter = ViewPagerFragmentAdapter(this, vpFactoryFragment)
        binding.viewPager.adapter = vpAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = vpFactoryFragment.getTitle(requireContext(), position)
        }.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onBackPressed(): Boolean {
        if (binding.searchLayout.isSearch) {
            binding.searchLayout.closeSearch()
            return true
        }

        return false
    }
}
