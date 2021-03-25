package ru.fasdev.tfs.view.ui.fragment.people

import android.os.Bundle
import android.view.*
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
import ru.fasdev.tfs.view.ui.global.fragmentRouter.ProvideFragmentRouter
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
        get() = (requireActivity() as ProvideFragmentRouter).getRouter()

    private val testUsersRepo = TestUserRepoImpl()
    private val usersInteractor: UserInteractor = UserInteractorImpl(testUsersRepo)

    private val holderFactory by lazy { PeopleHolderFactory(this) }
    private val adapter by lazy { BaseAdapter<ViewType>(holderFactory) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        view?.let { _binding = FragmentPeopleBinding.bind(it) }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUsers.addItemDecoration(VerticalSpaceItemDecoration(17.toDp))
        binding.rvUsers.adapter = adapter
        adapter.items = usersInteractor.getAllUsers().mapToUserUi { usersInteractor.isOnlineUser(it) }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_search -> {
                    stateSearchLayout(true)
                    true
                }
                else -> false
            }
        }

        binding.backBtnSearch.setOnClickListener {
            stateSearchLayout(false)
        }
    }

    private fun stateSearchLayout(isSearch: Boolean) {
        if (isSearch) {
            binding.searchLayout.visibility = View.VISIBLE
            binding.toolbar.visibility = View.INVISIBLE

            binding.edtSearch.isFocusable = true
        }
        else {
            binding.searchLayout.visibility = View.GONE
            binding.toolbar.visibility = View.VISIBLE

            binding.edtSearch.clearFocus()
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