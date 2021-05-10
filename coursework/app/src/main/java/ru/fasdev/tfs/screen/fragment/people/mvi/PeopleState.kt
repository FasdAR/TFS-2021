package ru.fasdev.tfs.screen.fragment.people.mvi

import ru.fasdev.tfs.mviCore.entity.state.UiState
import ru.fasdev.tfs.recycler.base.viewHolder.ViewType

data class PeopleState(
    val users: List<ViewType>? = null,
    val isLoading: Boolean = false,
    val error: Throwable? = null
) : UiState
