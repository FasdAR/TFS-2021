package ru.fasdev.tfs.screen.fragment.ownProfile.mvi

import ru.fasdev.tfs.domain.user.model.User
import ru.fasdev.tfs.mviCore.entity.action.SideAction
import ru.fasdev.tfs.mviCore.entity.action.UiAction

class OwnProfileAction {
    sealed class Ui : UiAction {
        object LoadUser : Ui()
    }

    sealed class Internal : SideAction {
        object LoadingUser : Internal()
        class LoadedUser(val user: User) : Internal()
        class LoadedError(val error: Throwable) : Internal()
    }
}
