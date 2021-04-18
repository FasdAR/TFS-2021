package ru.fasdev.tfs.mvi

import ru.fasdev.tfs.mvi.action.Action
import ru.fasdev.tfs.mvi.state.State

interface Middleware<S: State, A: Action>
{
    fun middleware(action: A, state: S)
}