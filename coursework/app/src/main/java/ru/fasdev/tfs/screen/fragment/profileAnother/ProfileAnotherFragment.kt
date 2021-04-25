package ru.fasdev.tfs.screen.fragment.profileAnother

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.fasdev.tfs.R
import ru.fasdev.tfs.TfsApp
import ru.fasdev.tfs.core.ext.setSystemInsetsInTop
import ru.fasdev.tfs.data.mapper.toUserUi
import ru.fasdev.tfs.databinding.FragmentAnotherProfileBinding
import ru.fasdev.tfs.di.module.UserDomainModule
import ru.fasdev.tfs.di.provide.ProvideFragmentRouter
import ru.fasdev.tfs.domain.user.interactor.UserInteractor
import ru.fasdev.tfs.domain.user.interactor.UserInteractorImpl
import ru.fasdev.tfs.fragmentRouter.FragmentRouter
import ru.fasdev.tfs.fragmentRouter.FragmentScreen
import ru.fasdev.tfs.screen.fragment.cardProfile.CardProfileFragment

class ProfileAnotherFragment : Fragment(R.layout.fragment_another_profile) {
    companion object {
        private val TAG: String = ProfileAnotherFragment::class.java.simpleName

        private const val NULL_USER = -1L
        private const val KEY_ID_USER = "id_user"

        fun newInstance(idUser: Long): ProfileAnotherFragment {
            return ProfileAnotherFragment().apply {
                arguments = bundleOf(KEY_ID_USER to idUser)
            }
        }

        fun getScreen(idUser: Long) = FragmentScreen(TAG, newInstance(idUser))
    }

    object ProfileAnotherComponent {
        private val userRepo = UserDomainModule.getUserRepo(TfsApp.AppComponent.userApi)
        val userInteractor = UserInteractorImpl(userRepo)
    }

    private var _binding: FragmentAnotherProfileBinding? = null
    private val binding get() = _binding!!

    private val rootRouter: FragmentRouter
        get() = (requireActivity() as ProvideFragmentRouter).getRouter()

    private val userInteractor: UserInteractor = ProfileAnotherComponent.userInteractor

    private val idUser: Long get() = arguments?.getLong(KEY_ID_USER, NULL_USER) ?: NULL_USER

    private val compositeDisposable = CompositeDisposable()
    private val cardProfile
        get() = childFragmentManager.findFragmentById(R.id.card_profile) as CardProfileFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        childFragmentManager.addFragmentOnAttachListener { _, fragment ->
            val cardProfile = fragment as CardProfileFragment
            cardProfile.lifecycle.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
                fun onResume() {
                    loadProfileData()
                }
            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)?.apply {
            _binding = FragmentAnotherProfileBinding.bind(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarLayout.apply {
            root.setSystemInsetsInTop()
            title.text = resources.getString(R.string.profile)
            btnNav.isVisible = true
            btnNav.setOnClickListener { rootRouter.back() }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
        _binding = null
    }

    private fun onError(error: Throwable) {
        Snackbar.make(binding.root, error.message.toString(), Snackbar.LENGTH_LONG).show()
    }

    // #region Rx chains
    private fun loadProfileData() {
        compositeDisposable.addAll(
            userInteractor.getUserById(idUser)
                .subscribeOn(Schedulers.io())
                .flatMap { user ->
                    userInteractor.getStatusUser(user.email)
                        .map { user.toUserUi(it) }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
                        cardProfile.status = it.userStatus
                        cardProfile.avatarSrc = it.avatarSrc
                        cardProfile.fullName = it.fullName
                    },
                    onError = ::onError
                )
        )
    }
    // #endregion
}
