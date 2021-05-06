package ru.fasdev.tfs.mviCore

import io.reactivex.Observable
import ru.fasdev.tfs.mviCore.entity.action.Action
import ru.fasdev.tfs.mviCore.entity.state.UiState

interface Middleware<A: Action, S: UiState>
{
    fun handle(actions: Observable<A>, state: Observable<S>): Observable<A>
}