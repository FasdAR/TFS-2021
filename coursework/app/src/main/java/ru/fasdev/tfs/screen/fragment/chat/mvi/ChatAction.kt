package ru.fasdev.tfs.screen.fragment.chat.mvi

import ru.fasdev.tfs.mviCore.entity.action.SideAction
import ru.fasdev.tfs.mviCore.entity.action.UiAction
import ru.fasdev.tfs.recycler.base.viewHolder.ViewType

sealed class ChatAction {
    sealed class Ui: UiAction {
        class LoadPageMessages(val direction: Int) : Ui()
        class SendMessage(val textMessage: String) : Ui()
        class SelectedReaction(val idMessage: Long? = null, val emoji: String) : Ui()
        class UnSelectedReaction(val idMessage: Long? = null, val emoji: String) : Ui()
        class LoadStreamInfo(val idStream: Long) : Ui()
        class LoadTopicInfo(val idTopic: Long) : Ui()
        class OpenEmojiDialog(val idMessage: Long?) : Ui()
    }

    sealed class Internal : SideAction {
        object LoadingPage: Internal()
        class LoadedPage(val items: List<ViewType>, val direction: Int): Internal()
        class LoadedError(val error: Throwable): Internal()
        class UpdateMessage(val item: ViewType) : Internal()
        class LoadedStreamName(val streamName: String) : Internal()
        class LoadedTopicName(val topicName: String) : Internal()
    }
}