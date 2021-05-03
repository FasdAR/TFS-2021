package ru.fasdev.tfs.screen.fragment.people

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import ru.fasdev.tfs.R
import ru.fasdev.tfs.core.ext.setSystemInsetsInTop
import ru.fasdev.tfs.databinding.FragmentPeopleBinding
import ru.fasdev.tfs.di.provide.ProvideFragmentRouter
import ru.fasdev.tfs.fragmentRouter.FragmentRouter
import ru.fasdev.tfs.fragmentRouter.FragmentScreen
import ru.fasdev.tfs.fragmentRouter.OnBackPressedListener
import ru.fasdev.tfs.recycler.adapter.RecyclerAdapter
import ru.fasdev.tfs.recycler.viewHolder.ViewType
import ru.fasdev.tfs.screen.fragment.people.mvi.PeopleAction
import ru.fasdev.tfs.screen.fragment.people.mvi.PeopleState
import ru.fasdev.tfs.screen.fragment.people.recycler.PeopleHolderFactory
import ru.fasdev.tfs.screen.fragment.people.recycler.viewHolder.UserViewHolder
import ru.fasdev.tfs.screen.fragment.profileAnother.ProfileAnotherFragment
import ru.fasdev.tfs.view.MviView
import ru.fasdev.tfs.view.searchToolbar.SearchToolbar

class PeopleFragment : Fragment(R.layout.fragment_people),
    UserViewHolder.OnClickUserListener,
    OnBackPressedListener, MviView<PeopleState> {

    companion object {
        val TAG: String = PeopleFragment::class.java.simpleName
        fun newInstance(): PeopleFragment = PeopleFragment()
        fun getScreen() = FragmentScreen(TAG, newInstance())
    }

    private var _binding: FragmentPeopleBinding? = null
    private val binding get() = _binding!!

    private val rootRouter: FragmentRouter
        get() = (requireActivity() as ProvideFragmentRouter).getRouter()

    private val holderFactory by lazy { PeopleHolderFactory(this) }
    private val adapter by lazy { RecyclerAdapter<ViewType>(holderFactory) }

    private val viewModel: PeopleViewModel by viewModels()

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
                viewModel.input.accept(
                    PeopleAction.SideEffectSearchUsers(query)
                )
            }
        }

        binding.rvUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUsers.adapter = adapter

        viewModel.attachView(this)
        viewModel.input.accept(PeopleAction.SideEffectLoadUsers)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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

    fun onError(throwable: Throwable) {
        Snackbar.make(binding.root, throwable.message.toString(), Snackbar.LENGTH_LONG).show()
    }

    override fun render(state: PeopleState) {
        when {
            state.error != null -> {
                onError(state.error)
            }
            state.isLoading -> {
                //TODO: В курсовой добавлю шиммер
                Log.d("IS_LOADING", "LOADING")
            }
            else -> {
                adapter.items = state.users
            }
        }
    }
    // #endregion
}
