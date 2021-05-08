package ru.fasdev.tfs.screen.fragment.people.mvi

import ru.fasdev.tfs.mviCore.entity.action.SideAction
import ru.fasdev.tfs.mviCore.entity.action.UiAction
import ru.fasdev.tfs.recycler.item.user.UserItem

class PeopleAction
{
    sealed class Ui: UiAction {
        object LoadUsers: Ui()
        class SearchUsers(val query: String): Ui()
    }

    sealed class Internal: SideAction {
        object LoadingUsers: Internal()
        class LoadedUsers(val users: List<UserItem>): Internal()
        class LoadedError(val error: Throwable): Internal()
    }
}