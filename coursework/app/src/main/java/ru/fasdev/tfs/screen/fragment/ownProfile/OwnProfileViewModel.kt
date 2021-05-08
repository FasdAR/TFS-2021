package ru.fasdev.tfs.screen.fragment.ownProfile

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import ru.fasdev.tfs.TfsApp
import ru.fasdev.tfs.data.newPck.repository.users.UsersRepositoryImpl
import ru.fasdev.tfs.mviCore.MviView
import ru.fasdev.tfs.mviCore.Store
import ru.fasdev.tfs.mviCore.entity.action.Action
import ru.fasdev.tfs.screen.fragment.ownProfile.mvi.OwnProfileAction
import ru.fasdev.tfs.screen.fragment.ownProfile.mvi.OwnProfileReducer
import ru.fasdev.tfs.screen.fragment.ownProfile.mvi.OwnProfileState
import ru.fasdev.tfs.screen.fragment.ownProfile.mvi.middleware.LoadUserMiddleware

class OwnProfileViewModel : ViewModel() {
    //#region Test DI
    object ProfileComponent {
        val usersRepository = UsersRepositoryImpl(TfsApp.AppComponent.newUserApi)
    }
    //#ednregion

    private val store: Store<Action, OwnProfileState> = Store(
        OwnProfileState(),
        OwnProfileReducer(),
        listOf(LoadUserMiddleware(ProfileComponent.usersRepository))
    )

    private val wiring = store.wire { actions -> actions.accept(OwnProfileAction.Ui.LoadUser) }
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