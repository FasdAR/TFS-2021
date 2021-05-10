package ru.fasdev.tfs.screen.fragment.channels

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.google.android.material.tabs.TabLayoutMediator
import com.jakewharton.rxrelay2.PublishRelay
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import ru.fasdev.tfs.R
import ru.fasdev.tfs.core.ext.setSystemInsetsInTop
import ru.fasdev.tfs.databinding.FragmentChannelsBinding
import ru.fasdev.tfs.fragmentRouter.FragmentScreen
import ru.fasdev.tfs.fragmentRouter.OnBackPressedListener
import ru.fasdev.tfs.pager.ViewPagerFragmentAdapter
import ru.fasdev.tfs.screen.fragment.channels.pager.StreamFragmentFactory
import ru.fasdev.tfs.view.searchToolbar.SearchToolbar
import javax.inject.Inject

class ChannelsFragment : Fragment(R.layout.fragment_channels), OnBackPressedListener,
    HasAndroidInjector {
    companion object {
        private val TAG: String = ChannelsFragment::class.java.simpleName
        fun newInstance(): ChannelsFragment = ChannelsFragment()
        fun getScreen() = FragmentScreen(TAG, newInstance())
    }

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    private var _binding: FragmentChannelsBinding? = null
    private val binding get() = _binding!!

    private val vpFactoryFragment = StreamFragmentFactory()
    private lateinit var vpAdapter: ViewPagerFragmentAdapter

    val provideSearch: PublishRelay<String> = PublishRelay.create()

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

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
                provideSearch.accept(query)
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
