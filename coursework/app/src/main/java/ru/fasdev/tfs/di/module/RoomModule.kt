package ru.fasdev.tfs.di.module

import android.content.Context
import androidx.room.Room
import ru.fasdev.tfs.data.source.db.TfsDatabase
import ru.fasdev.tfs.data.source.db.dao.*


class RoomModule
{
    companion object {
        fun getAppDatabase(context: Context): TfsDatabase {
            return Room.databaseBuilder(
                context,
                TfsDatabase::class.java, "tfs_db"
            ).build()
        }

        fun getStreamDao(appDatabase: TfsDatabase): StreamDao = appDatabase.streamDao()

        fun getTopicDao(appDatabase: TfsDatabase): TopicDao = appDatabase.topicDao()

        fun getMessageDao(appDatabase: TfsDatabase): MessageDao = appDatabase.messageDao()

        fun getUserDao(appDatabase: TfsDatabase): UserDao = appDatabase.userDao()

        fun getReactionDao(appDatabase: TfsDatabase): ReactionDao = appDatabase.reactionDao()
    }
}