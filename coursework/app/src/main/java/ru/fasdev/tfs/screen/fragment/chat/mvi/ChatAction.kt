package ru.fasdev.tfs.screen.fragment.chat.mvi

import ru.fasdev.tfs.domain.old.message.model.DirectionScroll
import ru.fasdev.tfs.recycler.viewHolder.ViewType

sealed class ChatAction {
    object LoadData : ChatAction()
    class ErrorLoading(val error: Throwable) : ChatAction()
    class LoadedData(val array: List<ViewType>) : ChatAction()

    class SideEffectLoadingPage(
        val topicName: String,
        val streamName: String,
        val lastVisibleId: Long,
        val direction: DirectionScroll
    ) : ChatAction()

    class SideEffectSelectedReaction(
        val idMessage: Int,
        val emoji: String,
        val isSelected: Boolean
    ) : ChatAction()

    class SideEffectSendMessage(
        val textMessage: String,
        val streamName: String,
        val topicName: String
    ) : ChatAction()
}