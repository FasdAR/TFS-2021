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
import io.reactivex.Observable
import io.reactivex.Observable.fromIterable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import ru.fasdev.tfs.R
import ru.fasdev.tfs.TfsApp
import ru.fasdev.tfs.core.ext.setSystemInsetsInTop
import ru.fasdev.tfs.data.mapper.toUserUi
import ru.fasdev.tfs.databinding.FragmentPeopleBinding
import ru.fasdev.tfs.di.module.UserDomainModule
import ru.fasdev.tfs.di.provide.ProvideFragmentRouter
import ru.fasdev.tfs.domain.user.interactor.UserInteractorImpl
import ru.fasdev.tfs.domain.user.model.User
import ru.fasdev.tfs.fragmentRouter.FragmentRouter
import ru.fasdev.tfs.fragmentRouter.FragmentScreen
import ru.fasdev.tfs.fragmentRouter.ProviderBackPressed
import ru.fasdev.tfs.recycler.adapter.RecyclerAdapter
import ru.fasdev.tfs.recycler.viewHolder.ViewType
import ru.fasdev.tfs.screen.fragment.people.mvi.PeopleAction
import ru.fasdev.tfs.screen.fragment.people.recycler.PeopleHolderFactory
import ru.fasdev.tfs.screen.fragment.people.recycler.viewHolder.UserViewHolder
import ru.fasdev.tfs.screen.fragment.people.recycler.viewType.UserUi
import ru.fasdev.tfs.screen.fragment.profileAnother.ProfileAnotherFragment
import ru.fasdev.tfs.view.searchToolbar.SearchToolbar
import java.util.concurrent.TimeUnit

class PeopleFragment :
    Fragment(R.layout.fragment_people),
    UserViewHolder.OnClickUserListener,
    ProviderBackPressed {

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
    private var disposable: Disposable? = null

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

        disposable = viewModel.store.subscribe {
            when {
                it.error != null -> {
                    onError(it.error)
                }
                it.isLoading -> {
                    Log.d("IS_LOADING", "LOADING")
                }
                else -> {
                    adapter.items = it.users
                }
            }
        }

        viewModel.input.accept(PeopleAction.SideEffectLoadUsers)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        disposable?.dispose()
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

    private fun onError(error: Throwable) {
        Snackbar.make(binding.root, error.message.toString(), Snackbar.LENGTH_LONG).show()
    }
}
