package ru.fasdev.tfs.screen.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.fasdev.tfs.R
import ru.fasdev.tfs.TfsApp
import ru.fasdev.tfs.data.mapper.toUserUi
import ru.fasdev.tfs.databinding.FragmentProfileBinding
import ru.fasdev.tfs.di.module.UserDomainModule
import ru.fasdev.tfs.domain.user.interactor.UserInteractorImpl
import ru.fasdev.tfs.fragmentRouter.FragmentScreen
import ru.fasdev.tfs.screen.fragment.cardProfile.CardProfileFragment

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    companion object {
        private val TAG: String = ProfileFragment::class.java.simpleName
        fun newInstance(): ProfileFragment = ProfileFragment()
        fun getScreen() = FragmentScreen(TAG, newInstance())
    }

    object ProfileComponent {
        private val userRepo = UserDomainModule.getUserRepo(TfsApp.AppComponent.userApi)
        val userInteractor = UserInteractorImpl(userRepo)
    }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val usersInteractor = ProfileComponent.userInteractor

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)?.apply {
            _binding = FragmentProfileBinding.bind(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cardProfile = childFragmentManager.findFragmentById(R.id.card_profile) as CardProfileFragment

        usersInteractor.getOwnUser()
            .subscribeOn(Schedulers.io())
            .flatMap { user ->
                usersInteractor.getStatusUser(user.email)
                    .map { user.toUserUi(it) }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    cardProfile.fullName = it.fullName
                    cardProfile.avatarSrc = it.avatarSrc
                    cardProfile.status = it.userStatus
                }
            )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
