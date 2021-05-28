package ru.fasdev.tfs.screen.fragment.anotherProfile.mvi

import ru.fasdev.tfs.domain.user.model.User
import ru.fasdev.tfs.mviCore.entity.action.SideAction
import ru.fasdev.tfs.mviCore.entity.action.UiAction

class AnotherProfileAction {
    sealed class Ui : UiAction {
        class LoadUser(val id: Long) : Ui()
    }

    sealed class Internal : SideAction {
        object LoadingUser : Internal()
        class LoadedUser(val user: User) : Internal()
        class LoadedError(val error: Throwable) : Internal()
    }
}
