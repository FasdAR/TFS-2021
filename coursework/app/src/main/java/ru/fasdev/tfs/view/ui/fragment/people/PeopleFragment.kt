package ru.fasdev.tfs.view.ui.fragment.people

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.PublishSubject
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.FragmentPeopleBinding
import ru.fasdev.tfs.domain.user.interactor.UserInteractor
import ru.fasdev.tfs.domain.user.interactor.UserInteractorImpl
import ru.fasdev.tfs.domain.user.repo.TestUserRepoImpl
import ru.fasdev.tfs.view.di.ProvideFragmentRouter
import ru.fasdev.tfs.view.feature.mapper.mapToUserUi
import ru.fasdev.tfs.view.feature.mapper.toUserUi
import ru.fasdev.tfs.view.feature.util.setSystemInsetsInTop
import ru.fasdev.tfs.view.ui.fragment.people.adapter.PeopleHolderFactory
import ru.fasdev.tfs.view.ui.fragment.people.adapter.viewHolder.UserViewHolder
import ru.fasdev.tfs.view.ui.fragment.profileAnother.ProfileAnotherFragment
import ru.fasdev.tfs.view.ui.global.fragmentRouter.FragmentRouter
import ru.fasdev.tfs.view.ui.global.fragmentRouter.FragmentScreen
import ru.fasdev.tfs.view.ui.global.fragmentRouter.ImplBackPressed
import ru.fasdev.tfs.view.ui.global.recycler.base.BaseAdapter
import ru.fasdev.tfs.view.ui.global.recycler.base.ViewType
import ru.fasdev.tfs.view.ui.global.view.viewGroup.toolbar.SearchToolbar
import java.util.concurrent.TimeUnit

class PeopleFragment : Fragment(R.layout.fragment_people), UserViewHolder.OnClickUserListener,
    ImplBackPressed {
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

    private val searchSubject = PublishSubject.create<String>()
    private val compositeDisposable = CompositeDisposable()

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
            textChangeListener = SearchToolbar.TextChangeListener { query -> searchUser(query) }
        }

        binding.rvUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUsers.adapter = adapter

        loadAllUsers()

        compositeDisposable.add(
            searchSubject
                .debounce(500, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .switchMapSingle { if (it.isNotEmpty()) usersInteractor.searchUser(it) else usersInteractor.getAllUsers()}
                .flatMapSingle { items ->
                    Observable.fromIterable(items)
                        .concatMap { user ->
                            usersInteractor.getIsOnlineStatusUser(user.id)
                                .map { user.toUserUi(it) }
                                .toObservable()
                        }
                        .toList()
                }
                .subscribeBy(
                    onNext = {
                        adapter.items = it
                    },
                    onError = ::onError
                )
        )
    }

    private fun searchUser(query: String = "") {
        searchSubject.onNext(query)
    }

    private fun loadAllUsers() {
        compositeDisposable.add(
            usersInteractor.getAllUsers()
                .flatMapObservable { items -> Observable.fromIterable(items) }
                .concatMap { item ->
                    usersInteractor.getIsOnlineStatusUser(item.id)
                        .map { item.toUserUi(it) }
                        .toObservable()
                }
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = { array ->
                        adapter.items = array
                    },
                    onError = ::onError
                )
        )
    }

    private fun onError(error: Throwable) {
        Snackbar.make(binding.root, error.message.toString(), Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
        _binding = null
    }

    override fun onClickUser(idUser: Int) {
        rootRouter.navigateTo(ProfileAnotherFragment.getScreen(idUser))
        binding.searchLayout.hideKeyboard()
    }

    override fun onBackPressed(): Boolean {
        if (binding.searchLayout.isSearch) {
            binding.searchLayout.closeSearch()
            return true
        }

        return false
    }
}
