package ru.fasdev.tfs.screen.fragment.ownProfile.mvi.middleware

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import ru.fasdev.tfs.data.newPck.repository.users.UsersRepository
import ru.fasdev.tfs.mviCore.Middleware
import ru.fasdev.tfs.mviCore.entity.action.Action
import ru.fasdev.tfs.screen.fragment.ownProfile.mvi.OwnProfileAction
import ru.fasdev.tfs.screen.fragment.ownProfile.mvi.OwnProfileState

class LoadUserMiddleware(private val usersRepository: UsersRepository) : Middleware<Action, OwnProfileState>
{
    override fun handle(actions: Observable<Action>, state: Observable<OwnProfileState>): Observable<Action> {
        return actions
            .ofType(OwnProfileAction.Ui.LoadUser.javaClass)
            .observeOn(Schedulers.io())
            .flatMap { _ ->
                return@flatMap usersRepository.getOwnUser()
                    .toObservable()
                    .map<OwnProfileAction.Internal> { OwnProfileAction.Internal.LoadedUser(it) }
                    .onErrorReturn { OwnProfileAction.Internal.LoadedError(it) }
                    .startWith(OwnProfileAction.Internal.LoadingUser)
            }
    }
}