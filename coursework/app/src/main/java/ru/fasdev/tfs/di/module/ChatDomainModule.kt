package ru.fasdev.tfs.di.module

import kotlinx.serialization.json.Json
import ru.fasdev.tfs.data.repo.MessageRepoImpl
import ru.fasdev.tfs.data.repo.UserRepoImpl
import ru.fasdev.tfs.data.source.network.chat.api.ChatApi
import ru.fasdev.tfs.data.source.network.users.api.UserApi
import ru.fasdev.tfs.domain.message.interactor.MessageInteractor
import ru.fasdev.tfs.domain.message.interactor.MessageInteractorImpl
import ru.fasdev.tfs.domain.message.repo.MessageRepo
import ru.fasdev.tfs.domain.user.interactor.UserInteractor
import ru.fasdev.tfs.domain.user.interactor.UserInteractorImpl
import ru.fasdev.tfs.domain.user.repo.UserRepo

class ChatDomainModule
{
    companion object {
        fun getMessageRepo(chatApi: ChatApi, json: Json): MessageRepo = MessageRepoImpl(chatApi, json)
        fun getMessageInteractor(messageRepo: MessageRepo): MessageInteractor = MessageInteractorImpl(messageRepo)
    }
}