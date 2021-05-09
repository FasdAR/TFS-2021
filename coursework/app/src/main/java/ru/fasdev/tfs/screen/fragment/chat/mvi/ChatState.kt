package ru.fasdev.tfs.screen.fragment.chat.mvi

import ru.fasdev.tfs.mviCore.entity.state.UiState
import ru.fasdev.tfs.recycler.base.viewHolder.ViewType

data class ChatState (
    val streamName: String? = null,
    val topicName: String? = null,
    val isLoading: Boolean = false,
    val idSelectedMessage: Long? = null,
    val items: List<ViewType> = emptyList(),
    val error: Throwable? = null,
) : UiState