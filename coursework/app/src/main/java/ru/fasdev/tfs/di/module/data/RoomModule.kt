package ru.fasdev.tfs.di.module.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.fasdev.tfs.data.source.database.TfsDatabase
import ru.fasdev.tfs.data.source.database.dao.MessageDao
import ru.fasdev.tfs.data.source.database.dao.ReactionDao
import ru.fasdev.tfs.data.source.database.dao.StreamDao
import ru.fasdev.tfs.data.source.database.dao.TopicDao
import ru.fasdev.tfs.data.source.database.dao.UserDao
import ru.fasdev.tfs.di.scope.AppScope

@Module
class RoomModule {
    @Provides
    @AppScope
    fun provideTfsDatabase(context: Context): TfsDatabase {
        return Room.databaseBuilder(context, TfsDatabase::class.java, "tfs_db").build()
    }

    @Provides
    @AppScope
    fun provideStreamDao(tfsDatabase: TfsDatabase): StreamDao {
        return tfsDatabase.streamDao()
    }

    @Provides
    @AppScope
    fun provideTopicDao(tfsDatabase: TfsDatabase): TopicDao {
        return tfsDatabase.topicDao()
    }

    @Provides
    @AppScope
    fun provideMessageDao(tfsDatabase: TfsDatabase): MessageDao {
        return tfsDatabase.messageDao()
    }

    @Provides
    @AppScope
    fun provideReactionDao(tfsDatabase: TfsDatabase): ReactionDao {
        return tfsDatabase.reactionDao()
    }

    @Provides
    @AppScope
    fun provideUserDao(tfsDatabase: TfsDatabase): UserDao {
        return tfsDatabase.userDao()
    }
}
