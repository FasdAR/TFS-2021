package ru.fasdev.tfs.screen.fragment.streamList.mvi

import ru.fasdev.tfs.mviCore.entity.state.UiState
import ru.fasdev.tfs.recycler.base.viewHolder.ViewType

data class StreamListState(
    val items: List<ViewType> = emptyList(),
    val isLoading: Boolean = false,
    val error: Throwable? = null,
) : UiState
