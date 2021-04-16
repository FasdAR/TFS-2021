package ru.fasdev.tfs.di.module

import kotlinx.serialization.json.Json
import ru.fasdev.tfs.data.repo.MessageRepoImpl
import ru.fasdev.tfs.data.source.db.dao.MessageDao
import ru.fasdev.tfs.data.source.db.dao.ReactionDao
import ru.fasdev.tfs.data.source.db.dao.UserDao
import ru.fasdev.tfs.data.source.network.chat.api.ChatApi
import ru.fasdev.tfs.domain.message.interactor.MessageInteractor
import ru.fasdev.tfs.domain.message.interactor.MessageInteractorImpl
import ru.fasdev.tfs.domain.message.repo.MessageRepo

class ChatDomainModule {
    companion object {
        fun getMessageRepo(chatApi: ChatApi, json: Json, messageDao: MessageDao, userDao: UserDao, reactionDao: ReactionDao): MessageRepo = MessageRepoImpl(chatApi, json, messageDao, userDao, reactionDao)
        fun getMessageInteractor(messageRepo: MessageRepo): MessageInteractor = MessageInteractorImpl(messageRepo)
    }
}
