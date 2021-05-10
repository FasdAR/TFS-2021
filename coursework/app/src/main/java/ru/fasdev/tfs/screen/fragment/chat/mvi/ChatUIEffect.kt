package ru.fasdev.tfs.screen.fragment.chat.mvi

sealed class ChatUiEffect
{
    class ErrorSnackbar(text: String): ChatUiEffect()
    object OpenEmojiDialog: ChatUiEffect()
}