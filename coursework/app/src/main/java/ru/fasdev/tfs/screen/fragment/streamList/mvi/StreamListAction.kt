package ru.fasdev.tfs.screen.fragment.streamList.mvi

import ru.fasdev.tfs.mviCore.entity.action.SideAction
import ru.fasdev.tfs.mviCore.entity.action.UiAction
import ru.fasdev.tfs.recycler.base.viewHolder.ViewType

sealed class StreamListAction {
    sealed class Ui : UiAction {
        object LoadAllStreams: Ui()
        object LoadSubStreams: Ui()
        class LoadTopics(val idStream: Long): Ui()
        class RemoveTopics(val idStream: Long): Ui()
        class SearchStreams(val query: String): Ui()
    }

    sealed class Internal : SideAction {
        object LoadingStreams: Internal()
        class LoadedTopics(val idStream: Long, val topics: List<ViewType>): Internal()
        class RemoveTopics(val idStream: Long): Internal()
        class LoadedStreams(val streams: List<ViewType>): Internal()
        class LoadedError(val error: Throwable): Internal()
    }
}