package ru.fasdev.tfs.di.module

import androidx.room.RoomDatabase
import kotlinx.serialization.json.Json
import ru.fasdev.tfs.data.old.repo.MessageRepoImpl
import ru.fasdev.tfs.data.old.source.db.dao.MessageDao
import ru.fasdev.tfs.data.old.source.db.dao.ReactionDao
import ru.fasdev.tfs.data.old.source.db.dao.UserDao
import ru.fasdev.tfs.data.old.source.network.chat.api.ChatApi
import ru.fasdev.tfs.domain.old.message.interactor.MessageInteractor
import ru.fasdev.tfs.domain.old.message.interactor.MessageInteractorImpl
import ru.fasdev.tfs.domain.old.message.repo.MessageRepo

class ChatDomainModule {
    companion object {
        fun getMessageRepo(
            chatApi: ChatApi,
            json: Json,
            roomDatabase: RoomDatabase,
            messageDao: MessageDao,
            userDao: UserDao,
            reactionDao: ReactionDao
        ): MessageRepo = MessageRepoImpl(chatApi, json, roomDatabase, messageDao, userDao, reactionDao)

        fun getMessageInteractor(messageRepo: MessageRepo): MessageInteractor =
            MessageInteractorImpl(messageRepo)
    }
}