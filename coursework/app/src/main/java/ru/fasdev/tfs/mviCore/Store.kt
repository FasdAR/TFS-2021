package ru.fasdev.tfs.mviCore

import ru.fasdev.tfs.mviCore.entity.action.Action
import ru.fasdev.tfs.mviCore.entity.state.UiState

interface Store <A: Action, S: UiState>
{
    val initialState: S
    val reducer: Reducer<S, A>
    val middlewares: List<Middleware<A, S>>
}