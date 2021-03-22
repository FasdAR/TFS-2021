package ru.fasdev.tfs.view.ui.fragment.people

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.FragmentPeopleBinding
import ru.fasdev.tfs.domain.user.interactor.UserInteractor
import ru.fasdev.tfs.domain.user.interactor.UserInteractorImpl
import ru.fasdev.tfs.domain.user.repo.TestUserRepoImpl
import ru.fasdev.tfs.view.feature.mapper.mapToUserUi
import ru.fasdev.tfs.view.feature.util.toDp
import ru.fasdev.tfs.view.ui.fragment.people.adapter.PeopleHolderFactory
import ru.fasdev.tfs.view.ui.fragment.people.adapter.viewHolder.UserViewHolder
import ru.fasdev.tfs.view.ui.fragment.profileAnother.ProfileAnotherFragment
import ru.fasdev.tfs.view.ui.global.fragmentRouter.FragmentRouter
import ru.fasdev.tfs.view.ui.global.recycler.base.BaseAdapter
import ru.fasdev.tfs.view.ui.global.recycler.base.ViewType
import ru.fasdev.tfs.view.ui.global.recycler.itemDecoration.VerticalSpaceItemDecoration

class PeopleFragment : Fragment(R.layout.fragment_people), UserViewHolder.OnClickUserListener
{
    companion object {
        val TAG: String = PeopleFragment::class.java.simpleName
        fun newInstance(): PeopleFragment = PeopleFragment()
    }

    private var _binding: FragmentPeopleBinding? = null
    private val binding get() = _binding!!

    private val rootRouter: FragmentRouter
        get() = requireActivity() as FragmentRouter

    private val testUsersRepo = TestUserRepoImpl()
    private val usersInteractor: UserInteractor = UserInteractorImpl(testUsersRepo)

    private val holderFactory by lazy { PeopleHolderFactory(this) }
    private val adapter by lazy { BaseAdapter<ViewType>(holderFactory) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        view?.let { _binding = FragmentPeopleBinding.bind(it) }
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUsers.addItemDecoration(VerticalSpaceItemDecoration(17.toDp))
        binding.rvUsers.adapter = adapter
        adapter.items = usersInteractor.getAllUsers()
                .mapToUserUi { usersInteractor.isOnlineUser(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_people_menu, menu)

        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.maxWidth = Int.MAX_VALUE
        searchView.queryHint = resources.getString(R.string.search_user_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean { return false }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.items = usersInteractor.searchUser(newText.toString())
                        .mapToUserUi { usersInteractor.isOnlineUser(it) }

                return true
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClickUser(idUser: Int) {
        val profileFragment = ProfileAnotherFragment.newInstance(idUser)
        rootRouter.navigateTo(profileFragment, ProfileAnotherFragment.TAG)
    }
}