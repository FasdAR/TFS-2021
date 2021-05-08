package ru.fasdev.tfs.screen.fragment.chat.mvi

import ru.fasdev.tfs.recycler.base.viewHolder.ViewType

data class ChatState (
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val listItems: List<ViewType> = emptyList()
)