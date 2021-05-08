package ru.fasdev.tfs.screen.fragment.streamList.mvi

import ru.fasdev.tfs.recycler.base.viewHolder.ViewType

data class StreamListState(
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val itemsList: List<ViewType> = emptyList()
)