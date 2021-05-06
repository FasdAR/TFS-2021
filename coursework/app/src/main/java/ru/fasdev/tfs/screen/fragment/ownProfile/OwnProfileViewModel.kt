package ru.fasdev.tfs.screen.fragment.ownProfile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.fasdev.tfs.TfsApp
import ru.fasdev.tfs.data.newPck.repository.users.UsersRepository
import ru.fasdev.tfs.data.newPck.repository.users.UsersRepositoryImpl
import ru.fasdev.tfs.domain.newPck.user.model.User
import java.util.concurrent.TimeUnit

class OwnProfileViewModel : ViewModel() {
    //#region Test DI
    object ProfileComponent {
        val usersRepository = UsersRepositoryImpl(TfsApp.AppComponent.newUserApi)
    }
    //#ednregion

    private val userRepository: UsersRepository = ProfileComponent.usersRepository

    private val compositeDisposable = CompositeDisposable()

    val userState: MutableLiveData<User?> = MutableLiveData()
    val errorState: MutableLiveData<String> = MutableLiveData()
    val isLoadingState: MutableLiveData<Boolean> = MutableLiveData()

    init {
        loadOwnUser()
    }

    fun loadOwnUser() {
        isLoadingState.postValue(true)

        compositeDisposable += userRepository.getOwnUser()
            .delay(1000L, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy (
                onError = {
                    errorState.postValue(it.toString())
                    isLoadingState.postValue(false)
                },
                onSuccess = {
                    userState.postValue(it)
                    isLoadingState.postValue(false)
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}