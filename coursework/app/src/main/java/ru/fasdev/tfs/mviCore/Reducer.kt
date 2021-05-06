package ru.fasdev.tfs.mviCore

import ru.fasdev.tfs.mviCore.entity.action.Action
import ru.fasdev.tfs.mviCore.entity.state.UiState

interface Reducer<S: UiState, A: Action>
{
    fun reduce(state: S, action: A): S
}