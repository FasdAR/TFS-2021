package ru.fasdev.tfs.view.ui.fragment.people

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.FragmentPeopleBinding
import ru.fasdev.tfs.domain.user.interactor.UserInteractor
import ru.fasdev.tfs.domain.user.interactor.UserInteractorImpl
import ru.fasdev.tfs.domain.user.repo.TestUserRepoImpl
import ru.fasdev.tfs.view.feature.mapper.mapToUserUi
import ru.fasdev.tfs.view.feature.util.*
import ru.fasdev.tfs.view.ui.fragment.people.adapter.PeopleHolderFactory
import ru.fasdev.tfs.view.ui.fragment.people.adapter.viewHolder.UserViewHolder
import ru.fasdev.tfs.view.ui.fragment.profileAnother.ProfileAnotherFragment
import ru.fasdev.tfs.view.ui.global.fragmentRouter.FragmentRouter
import ru.fasdev.tfs.view.ui.global.fragmentRouter.FragmentScreen
import ru.fasdev.tfs.view.ui.global.fragmentRouter.ProvideFragmentRouter
import ru.fasdev.tfs.view.ui.global.recycler.base.BaseAdapter
import ru.fasdev.tfs.view.ui.global.recycler.base.ViewType
import ru.fasdev.tfs.view.ui.global.recycler.itemDecoration.VerticalSpaceItemDecoration

class PeopleFragment : Fragment(R.layout.fragment_people), UserViewHolder.OnClickUserListener
{
    companion object {
        val TAG: String = PeopleFragment::class.java.simpleName
        fun newInstance(): PeopleFragment = PeopleFragment()
        fun getScreen() = FragmentScreen(TAG, newInstance())
    }

    private var _binding: FragmentPeopleBinding? = null
    private val binding get() = _binding!!

    private val rootRouter: FragmentRouter
        get() = (requireActivity() as ProvideFragmentRouter).getRouter()

    private val testUsersRepo = TestUserRepoImpl()
    private val usersInteractor: UserInteractor = UserInteractorImpl(testUsersRepo)

    private val holderFactory by lazy { PeopleHolderFactory(this) }
    private val adapter by lazy { BaseAdapter<ViewType>(holderFactory) }

    private val isSearch
        get() = binding.searchLayout.root.visibility == View.VISIBLE

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        view?.let { _binding = FragmentPeopleBinding.bind(it) }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbarLayout.apply {
            root.setSystemInsetsInTop()
            title.text = resources.getString(R.string.users)
            btnSearch.isVisible = true
            btnSearch.setOnClickListener {
                searchLayoutState(true)
            }
        }

        binding.searchLayout.apply {
            root.setSystemInsetsInTop()
            btnBackSearch.setOnClickListener {
                searchLayoutState(false)
            }

            searchEdt.addTextChangedListener {
                if (!it.isNullOrEmpty()) searchUser(it.toString())
            }
        }

        binding.rvUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUsers.adapter = adapter
        adapter.items = usersInteractor.getAllUsers().mapToUserUi { usersInteractor.isOnlineUser(it) }
    }

    private fun searchUser(query: String = "") {
        adapter.items = usersInteractor.searchUser(query)
                .mapToUserUi { usersInteractor.isOnlineUser(it) }
    }

    private fun searchLayoutState(isSearch: Boolean)
    {
        if (isSearch) {
            binding.toolbarLayout.root.visibility = View.INVISIBLE

            binding.searchLayout.root.visibility = View.VISIBLE
            binding.searchLayout.searchEdt.showKeyboard()
        } else {
            binding.toolbarLayout.root.visibility = View.VISIBLE

            binding.searchLayout.root.visibility = View.GONE
            binding.searchLayout.searchEdt.hideKeyboard()

            searchUser()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClickUser(idUser: Int) {
        rootRouter.navigateTo(ProfileAnotherFragment.getScreen(idUser))
    }
}