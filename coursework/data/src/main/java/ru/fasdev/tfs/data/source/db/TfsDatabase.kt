package ru.fasdev.tfs.data.source.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.fasdev.tfs.data.source.db.stream.dao.StreamDao
import ru.fasdev.tfs.data.source.db.stream.dao.TopicDao
import ru.fasdev.tfs.data.source.db.stream.model.StreamDB
import ru.fasdev.tfs.data.source.db.stream.model.TopicDB

@Database(
    entities = [
        StreamDB::class,
        TopicDB::class
    ],
    version = 1
)
abstract class TfsDatabase : RoomDatabase()
{
    abstract fun streamDao(): StreamDao
    abstract fun topicDao(): TopicDao
}