package ru.fasdev.tfs.screen.fragment.people

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observable.fromIterable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import ru.fasdev.tfs.R
import ru.fasdev.tfs.TfsApp
import ru.fasdev.tfs.databinding.FragmentPeopleBinding
import ru.fasdev.tfs.domain.user.interactor.UserInteractorImpl
import ru.fasdev.tfs.di.provide.ProvideFragmentRouter
import ru.fasdev.tfs.data.mapper.toUserUi
import ru.fasdev.tfs.core.ext.setSystemInsetsInTop
import ru.fasdev.tfs.di.module.UserDomainModule
import ru.fasdev.tfs.domain.user.model.User
import ru.fasdev.tfs.screen.fragment.people.recycler.PeopleHolderFactory
import ru.fasdev.tfs.screen.fragment.profileAnother.ProfileAnotherFragment
import ru.fasdev.tfs.fragmentRouter.FragmentRouter
import ru.fasdev.tfs.fragmentRouter.FragmentScreen
import ru.fasdev.tfs.fragmentRouter.ProviderBackPressed
import ru.fasdev.tfs.recycler.adapter.RecyclerAdapter
import ru.fasdev.tfs.recycler.viewHolder.ViewType
import ru.fasdev.tfs.screen.fragment.people.recycler.viewHolder.UserViewHolder
import ru.fasdev.tfs.screen.fragment.people.recycler.viewType.UserUi
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

    object PeopleComponent {
        val userRepo = UserDomainModule.getUserRepo(TfsApp.AppComponent.userApi)
        val userInteractor = UserInteractorImpl(userRepo)
    }

    private var _binding: FragmentPeopleBinding? = null
    private val binding get() = _binding!!

    private val rootRouter: FragmentRouter
        get() = (requireActivity() as ProvideFragmentRouter).getRouter()

    private val usersInteractor = PeopleComponent.userInteractor

    private val holderFactory by lazy { PeopleHolderFactory(this) }
    private val adapter by lazy { RecyclerAdapter<ViewType>(holderFactory) }

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
        observerSearch()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
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

    private fun onError(error: Throwable) {
        Snackbar.make(binding.root, error.message.toString(), Snackbar.LENGTH_LONG).show()
    }

    // #region Rx chains
    private fun searchUser(query: String = "") {
        searchSubject.onNext(query)
    }

    private fun Single<List<User>>.mapToUiUser(): Single<List<UserUi>> {
        return flatMapObservable(::fromIterable)
        .concatMap {
            Observable.just(it).delay(10, TimeUnit.MILLISECONDS)
        }
        .flatMapSingle { user ->
            usersInteractor.getStatusUser(user.email)
                .map { status -> user.toUserUi(status) }
                .subscribeOn(Schedulers.io())
        }
        .toList()
    }

    private fun loadAllUsers() {
        compositeDisposable.add(
            usersInteractor.getAllUsers()
                .subscribeOn(Schedulers.io())
                .mapToUiUser()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = { array ->
                        adapter.items = array
                    },
                    onError = ::onError
                )
        )
    }

    private fun observerSearch() {
        compositeDisposable.add(
            searchSubject
                .debounce(500, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .switchMapSingle {
                    if (it.isNotEmpty()) usersInteractor.searchUser(it)
                    else usersInteractor.getAllUsers()
                }
                .flatMapSingle {
                    Single.just(it)
                        .mapToUiUser()
                }
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn { error ->
                    onError(error)
                    return@onErrorReturn listOf()
                }
                .subscribeBy(
                    onNext = { array ->
                        adapter.items = array
                    }
                )
        )
    }
}
