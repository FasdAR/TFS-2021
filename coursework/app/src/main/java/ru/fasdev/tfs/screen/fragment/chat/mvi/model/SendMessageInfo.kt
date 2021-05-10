package ru.fasdev.tfs.screen.fragment.chat.mvi.model

import ru.fasdev.tfs.screen.fragment.chat.mvi.ChatAction

class SendMessageInfo(
    val streamName: String,
    val topicName: String,
    val action: ChatAction.Ui.SendMessage
)
