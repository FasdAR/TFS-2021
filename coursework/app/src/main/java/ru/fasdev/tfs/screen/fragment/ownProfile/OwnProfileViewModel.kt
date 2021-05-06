package ru.fasdev.tfs.screen.fragment.ownProfile

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import ru.fasdev.tfs.TfsApp
import ru.fasdev.tfs.data.newPck.repository.users.UsersRepositoryImpl
import ru.fasdev.tfs.mviCore.MviView
import ru.fasdev.tfs.mviCore.entity.action.Action
import ru.fasdev.tfs.screen.fragment.ownProfile.mvi.OwnProfileState
import ru.fasdev.tfs.screen.fragment.ownProfile.mvi.OwnProfileStore

class OwnProfileViewModel : ViewModel() {
    //#region Test DI
    object ProfileComponent {
        val usersRepository = UsersRepositoryImpl(TfsApp.AppComponent.newUserApi)
    }
    //#ednregion

    private val store: OwnProfileStore = OwnProfileStore(ProfileComponent.usersRepository)

    private val wiring = store.wire()
    private var viewBinding: Disposable = Disposables.empty()

    override fun onCleared() {
        super.onCleared()
        wiring.dispose()
    }

    fun bind(view: MviView<Action, OwnProfileState>) {
        viewBinding = store.bind(view)
    }

    fun unBind() {
        viewBinding.dispose()
    }
}