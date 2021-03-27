package ru.fasdev.tfs.view.ui.fragment.profileAnother

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.FragmentAnotherProfileBinding
import ru.fasdev.tfs.domain.user.interactor.UserInteractor
import ru.fasdev.tfs.domain.user.interactor.UserInteractorImpl
import ru.fasdev.tfs.domain.user.repo.TestUserRepoImpl
import ru.fasdev.tfs.view.di.ProvideFragmentRouter
import ru.fasdev.tfs.view.feature.util.setSystemInsetsInTop
import ru.fasdev.tfs.view.ui.fragment.cardProfile.CardProfileFragment
import ru.fasdev.tfs.view.ui.global.fragmentRouter.FragmentRouter
import ru.fasdev.tfs.view.ui.global.fragmentRouter.FragmentScreen

class ProfileAnotherFragment : Fragment(R.layout.fragment_another_profile) {
    companion object {
        val TAG: String = ProfileAnotherFragment::class.java.simpleName

        private const val NULL_USER = -1
        private const val KEY_ID_USER = "id_user"

        fun newInstance(idUser: Int): ProfileAnotherFragment {
            return ProfileAnotherFragment().apply {
                arguments = bundleOf(KEY_ID_USER to idUser)
            }
        }

        fun getScreen(idUser: Int) = FragmentScreen(TAG, newInstance(idUser))
    }

    private var _binding: FragmentAnotherProfileBinding? = null
    private val binding get() = _binding!!

    private val rootRouter: FragmentRouter
        get() = (requireActivity() as ProvideFragmentRouter).getRouter()

    private val testUserRepo = TestUserRepoImpl()
    private val userInteractor: UserInteractor = UserInteractorImpl(testUserRepo)

    private val idUser: Int get() = arguments?.getInt(KEY_ID_USER, NULL_USER) ?: NULL_USER

    private val compositeDisposable = CompositeDisposable()

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

        val cardProfile =
            childFragmentManager.findFragmentById(R.id.card_profile) as CardProfileFragment

        compositeDisposable.addAll(
            userInteractor.getUserById(idUser)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
                        cardProfile.fullName = it.fullName
                    },
                    onError = ::onError
                ),
            userInteractor.getStatusUser(idUser)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
                        cardProfile.status = it
                    },
                    onError = ::onError
                ),
            userInteractor.getIsOnlineStatusUser(idUser)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
                        cardProfile.isOnline = it
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
}
