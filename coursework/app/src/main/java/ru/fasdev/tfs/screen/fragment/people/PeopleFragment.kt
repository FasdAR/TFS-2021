package ru.fasdev.tfs.screen.fragment.people

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxrelay2.PublishRelay
import ru.fasdev.tfs.R
import ru.fasdev.tfs.core.ext.setSystemInsetsInTop
import ru.fasdev.tfs.databinding.FragmentPeopleBinding
import ru.fasdev.tfs.di.provide.ProvideFragmentRouter
import ru.fasdev.tfs.fragmentRouter.FragmentRouter
import ru.fasdev.tfs.fragmentRouter.FragmentScreen
import ru.fasdev.tfs.fragmentRouter.OnBackPressedListener
import ru.fasdev.tfs.mviCore.MviView
import ru.fasdev.tfs.mviCore.entity.action.Action
import ru.fasdev.tfs.recycler.base.adapter.RecyclerAdapter
import ru.fasdev.tfs.recycler.base.viewHolder.ViewType
import ru.fasdev.tfs.screen.fragment.people.mvi.PeopleAction
import ru.fasdev.tfs.screen.fragment.people.mvi.PeopleState
import ru.fasdev.tfs.screen.fragment.people.recycler.PeopleHolderFactory
import ru.fasdev.tfs.recycler.item.user.UserViewHolder
import ru.fasdev.tfs.screen.fragment.info.*
import ru.fasdev.tfs.screen.fragment.profileAnother.ProfileAnotherFragment
import ru.fasdev.tfs.view.searchToolbar.SearchToolbar

class PeopleFragment : Fragment(R.layout.fragment_people),
    UserViewHolder.OnClickUserListener,
    OnBackPressedListener, MviView<Action, PeopleState>, InfoPlaceholderFragment.Listener {

    companion object {
        private val TAG: String = PeopleFragment::class.java.simpleName
        private fun newInstance(): PeopleFragment = PeopleFragment()
        fun getScreen() = FragmentScreen(TAG, newInstance())
    }

    override val actions: PublishRelay<Action> = PublishRelay.create()
    private val viewModel: PeopleViewModel by viewModels()

    private var _binding: FragmentPeopleBinding? = null
    private val binding get() = _binding!!

    private val rootRouter: FragmentRouter
        get() = (requireActivity() as ProvideFragmentRouter).getRouter()

    private val holderFactory by lazy { PeopleHolderFactory(this) }
    private val adapter by lazy { RecyclerAdapter<ViewType>(holderFactory) }

    private val infoFragment get() = childFragmentManager.findFragmentById(R.id.info_placeholder) as InfoPlaceholderFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)?.apply {
            _binding = FragmentPeopleBinding.bind(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.toolbarLayout) {
            root.setSystemInsetsInTop()
            title.text = resources.getString(R.string.users)

            btnSearch.isVisible = true
            btnSearch.setOnClickListener {
                binding.searchLayout.openSearch()
            }
        }

        with(binding.searchLayout) {
            setSystemInsetsInTop()
            binding.searchLayout.attachToolbar = binding.toolbarLayout.root
            textChangeListener = SearchToolbar.TextChangeListener { query ->
                actions.accept(PeopleAction.Ui.SearchUsers(query))
            }
        }

        binding.usersRv.layoutManager = LinearLayoutManager(requireContext())
        binding.usersRv.adapter = adapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            if (binding.searchLayout.isSearch) {
                actions.accept(PeopleAction.Ui.SearchUsers(binding.searchLayout.text))
            } else {
                actions.accept(PeopleAction.Ui.LoadUsers)
            }
        }

        viewModel.bind(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        viewModel.unBind()
    }

    override fun onClickUser(idUser: Int, email: String) {
        rootRouter.navigateTo(ProfileAnotherFragment.getScreen(idUser.toLong()))
        binding.searchLayout.hideKeyboard()
    }

    override fun onBackPressed(): Boolean {
        if (binding.searchLayout.isSearch) {
            binding.searchLayout.closeSearch()
            return true
        }

        return false
    }

    override fun render(state: PeopleState) {
        binding.swipeRefreshLayout.isRefreshing = state.isLoading
        adapter.items = state.users ?: emptyList()

        if (state.error != null) {
            binding.usersRv.isGone = true
            binding.infoPlaceholder.isGone = false

            infoFragment.handleErrorState(state.error)
        }
        else {
            if (state.isLoading) {
                binding.infoPlaceholder.isGone = true
            }
            else {
                if (!state.users.isNullOrEmpty()) {
                    binding.infoPlaceholder.isGone = true
                    binding.usersRv.isGone = false
                }
                else {
                    binding.usersRv.isGone = true
                    binding.infoPlaceholder.isGone = false

                    infoFragment.emptyListState(resources.getString(R.string.empty_users_list))
                }
            }
        }
    }

    override fun onBtnClickInfoPlaceholder(buttonType: InfoPlaceholderFragment.ButtonType) {
        when (buttonType) {
            InfoPlaceholderFragment.ButtonType.POSITIVE -> actions.accept(PeopleAction.Ui.LoadUsers)
        }
    }
}