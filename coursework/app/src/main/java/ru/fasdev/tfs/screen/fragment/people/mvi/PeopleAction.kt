package ru.fasdev.tfs.screen.fragment.people.mvi

import ru.fasdev.tfs.screen.fragment.people.recycler.viewType.UserUi

sealed class PeopleAction
{
    object LoadUsers: PeopleAction()
    class ErrorLoading(val error: Throwable): PeopleAction()
    class LoadedUsers(val users: List<UserUi>): PeopleAction()

    class SideEffectSearchUsers(val query: String): PeopleAction()
    object SideEffectLoadUsers: PeopleAction()
}