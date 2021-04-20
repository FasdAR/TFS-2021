package ru.fasdev.tfs.screen.fragment.people.mvi

import ru.fasdev.tfs.recycler.viewHolder.ViewType

data class PeopleState (
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val users: List<ViewType> = listOf()
)