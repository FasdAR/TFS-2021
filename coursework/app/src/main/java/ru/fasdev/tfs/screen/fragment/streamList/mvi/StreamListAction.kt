package ru.fasdev.tfs.screen.fragment.streamList.mvi

import ru.fasdev.tfs.recycler.viewHolder.ViewType

sealed class StreamListAction {
    object LoadData: StreamListAction()
    class ErrorLoading(val error: Throwable): StreamListAction()
    class LoadedArray(val streams: List<ViewType>): StreamListAction()

    class SideEffectLoadAllStreams(val mode: Int): StreamListAction()
    class SideEffectSearchStreams(val query: String, val mode: Int): StreamListAction()
    class SideEffectLoadTopics(val idStream: Int, val opened: Boolean): StreamListAction()
}