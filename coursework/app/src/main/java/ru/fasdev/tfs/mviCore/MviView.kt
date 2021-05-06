package ru.fasdev.tfs.mviCore

import io.reactivex.Observable
import ru.fasdev.tfs.mviCore.entity.action.Action
import ru.fasdev.tfs.mviCore.entity.state.UiState

interface MviView<A: Action, S: UiState>
{
    val actions: Observable<A>
    fun render(state: S)
}