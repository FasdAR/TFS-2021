package ru.fasdev.tfs.screen.fragment.people.mvi

import ru.fasdev.tfs.domain.user.model.User
import ru.fasdev.tfs.screen.fragment.people.recycler.viewType.UserUi

sealed class PeopleAction
{
    object LoadUsers: PeopleAction()
    class ErrorLoading(val error: Throwable): PeopleAction()
    class LoadedUsers(val users: List<UserUi>): PeopleAction()

    object SideEffectLoadUsers: PeopleAction()
}