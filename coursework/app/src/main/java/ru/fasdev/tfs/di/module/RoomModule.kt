package ru.fasdev.tfs.di.module

import android.content.Context
import androidx.room.Room
import ru.fasdev.tfs.data.source.db.TfsDatabase
import ru.fasdev.tfs.data.source.db.stream.dao.StreamDao
import ru.fasdev.tfs.data.source.db.stream.dao.TopicDao


class RoomModule
{
    companion object {
        fun getAppDatabase(context: Context): TfsDatabase {
            return Room.databaseBuilder(
                context,
                TfsDatabase::class.java, "tfs_db"
            ).build()
        }

        fun getStreamDao(appDatabase: TfsDatabase): StreamDao {
            return appDatabase.streamDao()
        }

        fun getTopicDao(appDatabase: TfsDatabase): TopicDao {
            return appDatabase.topicDao()
        }
    }
}