package ru.fasdev.tfs.di.module

import android.content.Context
import androidx.room.Room
import ru.fasdev.tfs.data.old.source.db.TfsDatabase
import ru.fasdev.tfs.data.old.source.db.dao.*


class RoomModule
{
    companion object {
        fun getNewAppDatabase(context: Context): ru.fasdev.tfs.data.newPck.source.database.TfsDatabase {
            return Room.databaseBuilder(
                context,
                ru.fasdev.tfs.data.newPck.source.database.TfsDatabase::class.java, "tfs_db_new"
            ).build()
        }

        fun getAppDatabase(context: Context): TfsDatabase {
            return Room.databaseBuilder(
                context,
                TfsDatabase::class.java, "tfs_db"
            ).build()
        }

        fun getStreamDao(appDatabase: ru.fasdev.tfs.data.newPck.source.database.TfsDatabase): ru.fasdev.tfs.data.newPck.source.database.dao.StreamDao = appDatabase.streamDao()

        fun getTopicDao(appDatabase: ru.fasdev.tfs.data.newPck.source.database.TfsDatabase): ru.fasdev.tfs.data.newPck.source.database.dao.TopicDao = appDatabase.topicDao()

        fun getStreamDao(appDatabase: TfsDatabase): StreamDao = appDatabase.streamDao()

        fun getTopicDao(appDatabase: TfsDatabase): TopicDao = appDatabase.topicDao()

        fun getMessageDao(appDatabase: TfsDatabase): MessageDao = appDatabase.messageDao()

        fun getUserDao(appDatabase: TfsDatabase): UserDao = appDatabase.userDao()

        fun getReactionDao(appDatabase: TfsDatabase): ReactionDao = appDatabase.reactionDao()
    }
}