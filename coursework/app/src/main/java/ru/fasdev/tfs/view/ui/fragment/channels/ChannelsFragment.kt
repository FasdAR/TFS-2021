package ru.fasdev.tfs.view.ui.fragment.channels

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.FragmentChannelsBinding
import ru.fasdev.tfs.view.ui.bottomDialog.emoji.SelectEmojiBottomDialog
import ru.fasdev.tfs.view.ui.fragment.channels.viewPage.TopicFragmentFactory
import ru.fasdev.tfs.view.ui.fragment.topicList.TopicListFragment
import ru.fasdev.tfs.view.ui.global.viewPager.base.ViewPagerFragmentAdapter

class ChannelsFragment : Fragment(R.layout.fragment_channels)
{
    companion object {
        val TAG: String = ChannelsFragment::class.java.simpleName
        fun newInstance(): ChannelsFragment = ChannelsFragment()
    }

    private var _binding: FragmentChannelsBinding? = null
    private val binding get() = _binding!!

    private val vpFactoryFragment = TopicFragmentFactory()
    private val vpAdapter by lazy { ViewPagerFragmentAdapter(this, vpFactoryFragment) }

    private var searchView: SearchView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        view?.let { _binding = FragmentChannelsBinding.bind(it) }

        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = vpAdapter

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                searchView?.query?.let {
                    searchCurrentFragment(it.toString())
                }
            }
        })

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = vpFactoryFragment.getTitle(requireContext(), position)
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_people_menu, menu)

        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView?.maxWidth = Int.MAX_VALUE
        searchView?.queryHint = resources.getString(R.string.search)
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean { return false }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    searchCurrentFragment(it)
                }

                return true
            }
        })
    }

    private fun searchCurrentFragment(query: String) {
        val myFragment = childFragmentManager.findFragmentByTag("f" + binding.viewPager.currentItem)
        myFragment?.let {
            (it as TopicListFragment).search(query)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}