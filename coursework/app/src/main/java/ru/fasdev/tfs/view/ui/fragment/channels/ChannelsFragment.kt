package ru.fasdev.tfs.view.ui.fragment.channels

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.FragmentChannelsBinding
import ru.fasdev.tfs.view.ui.fragment.channels.viewPage.TopicFragmentFactory
import ru.fasdev.tfs.view.ui.global.fragmentRouter.FragmentScreen
import ru.fasdev.tfs.view.ui.global.viewPager.base.ViewPagerFragmentAdapter

class ChannelsFragment : Fragment(R.layout.fragment_channels)
{
    companion object {
        val TAG: String = ChannelsFragment::class.java.simpleName
        fun newInstance(): ChannelsFragment = ChannelsFragment()
        fun getScreen() = FragmentScreen(TAG, newInstance())
    }

    private var _binding: FragmentChannelsBinding? = null
    private val binding get() = _binding!!

    private val vpFactoryFragment = TopicFragmentFactory()
    private lateinit var vpAdapter: ViewPagerFragmentAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        view?.let { _binding = FragmentChannelsBinding.bind(it) }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vpAdapter = ViewPagerFragmentAdapter(this, vpFactoryFragment)

        binding.viewPager.adapter = vpAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = vpFactoryFragment.getTitle(requireContext(), position)
        }.attach()
    }

    /*
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
    }*/

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}