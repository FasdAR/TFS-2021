package ru.fasdev.tfs.di.module.data

import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import ru.fasdev.tfs.data.repository.messages.MessagesRepository
import ru.fasdev.tfs.data.repository.messages.MessagesRepositoryImpl
import ru.fasdev.tfs.data.repository.streams.StreamsRepository
import ru.fasdev.tfs.data.repository.streams.StreamsRepositoryImpl
import ru.fasdev.tfs.data.repository.users.UsersRepository
import ru.fasdev.tfs.data.repository.users.UsersRepositoryImpl
import ru.fasdev.tfs.data.source.database.dao.*
import ru.fasdev.tfs.data.source.network.events.api.EventsApi
import ru.fasdev.tfs.data.source.network.events.manager.EventsManager
import ru.fasdev.tfs.data.source.network.messages.api.MessagesApi
import ru.fasdev.tfs.data.source.network.streams.api.StreamsApi
import ru.fasdev.tfs.data.source.network.users.api.UsersApi
import ru.fasdev.tfs.di.scope.AppScope

@Module
class RepositoryModule {
    @Provides
    @AppScope
    fun eventManager(json: Json, eventsApi: EventsApi): EventsManager {
        return EventsManager(json, eventsApi)
    }

    @Provides
    @AppScope
    fun provideMessageRepository(
        json: Json, messagesApi: MessagesApi, eventsManager: EventsManager,
        messageDao: MessageDao, reactionDao: ReactionDao, usersDao: UserDao
    ): MessagesRepository {
        return MessagesRepositoryImpl(
            json,
            messagesApi,
            eventsManager,
            usersDao,
            messageDao,
            reactionDao
        )
    }

    @Provides
    @AppScope
    fun provideStreamsRepository(
        usersApi: UsersApi, streamsApi: StreamsApi, streamsDao: StreamDao, topicDao: TopicDao
    ): StreamsRepository {
        return StreamsRepositoryImpl(usersApi, streamsApi, streamsDao, topicDao)
    }

    @Provides
    @AppScope
    fun provideUsersRepository(usersApi: UsersApi): UsersRepository {
        return UsersRepositoryImpl(usersApi)
    }
}