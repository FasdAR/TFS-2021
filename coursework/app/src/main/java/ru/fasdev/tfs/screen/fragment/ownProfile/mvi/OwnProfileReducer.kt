package ru.fasdev.tfs.screen.fragment.ownProfile.mvi

import ru.fasdev.tfs.mviCore.Reducer
import ru.fasdev.tfs.mviCore.entity.action.Action

class OwnProfileReducer : Reducer<OwnProfileState, Action>
{
    override fun reduce(state: OwnProfileState, action: Action): OwnProfileState {
        return when (action) {
            is OwnProfileAction.Internal.LoadedError -> state.copy(isLoading = false, error = action.error)
            is OwnProfileAction.Internal.LoadedUser -> state.copy(isLoading = false, error = null, user = action.user)
            is OwnProfileAction.Internal.LoadingUser -> state.copy(isLoading = true, error = null)
            else -> state
        }
    }
}