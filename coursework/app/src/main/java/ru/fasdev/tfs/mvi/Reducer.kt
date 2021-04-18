package ru.fasdev.tfs.mvi

import ru.fasdev.tfs.mvi.action.Action
import ru.fasdev.tfs.mvi.state.State


interface Reducer<S: State, A: Action>
{
    fun reduce(action: A, state: S): S
}