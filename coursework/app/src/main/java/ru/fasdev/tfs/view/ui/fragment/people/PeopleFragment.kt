package ru.fasdev.tfs.view.ui.fragment.people

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.FragmentPeopleBinding
import ru.fasdev.tfs.domain.users.interactor.UsersInteractor
import ru.fasdev.tfs.domain.users.interactor.UsersInteractorImpl
import ru.fasdev.tfs.domain.users.repo.TestUsersRepoImpl
import ru.fasdev.tfs.view.feature.mapper.mapToUserUi
import ru.fasdev.tfs.view.feature.util.toDp
import ru.fasdev.tfs.view.ui.fragment.people.adapter.PeopleHolderFactory
import ru.fasdev.tfs.view.ui.global.recycler.base.BaseAdapter
import ru.fasdev.tfs.view.ui.global.recycler.base.ViewType
import ru.fasdev.tfs.view.ui.global.recycler.itemDecoration.VerticalSpaceItemDecoration

class PeopleFragment : Fragment(R.layout.fragment_people)
{
    companion object {
        val TAG: String = PeopleFragment::class.java.simpleName
        fun newInstance(): PeopleFragment = PeopleFragment()
    }

    private var _binding: FragmentPeopleBinding? = null
    private val binding get() = _binding!!

    private val testUsersRepo = TestUsersRepoImpl()
    private val usersInteractor: UsersInteractor = UsersInteractorImpl(testUsersRepo)

    private val holderFactory by lazy { PeopleHolderFactory() }
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
        adapter.items = usersInteractor.getAllUsers().mapToUserUi()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}