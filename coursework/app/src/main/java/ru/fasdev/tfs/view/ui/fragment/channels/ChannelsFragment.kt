package ru.fasdev.tfs.view.ui.fragment.channels

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.FragmentChannelsBinding
import ru.fasdev.tfs.view.feature.mapper.mapToUserUi
import ru.fasdev.tfs.view.ui.fragment.channels.viewPage.TopicFragmentFactory
import ru.fasdev.tfs.view.ui.global.viewPager.base.ViewPagerFragmentAdapter

class ChannelsFragment : Fragment(R.layout.fragment_channels)
{
    companion object {
        val TAG: String = ChannelsFragment::class.java.simpleName
        fun newInstance(): ChannelsFragment = ChannelsFragment()
    }

    private var _binding: FragmentChannelsBinding? = null
    private val binding get() = _binding!!

    private val vpAdapter by lazy { ViewPagerFragmentAdapter(this, TopicFragmentFactory()) }

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
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_people_menu, menu)

        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.maxWidth = Int.MAX_VALUE
        searchView.queryHint = resources.getString(R.string.search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean { return false }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}