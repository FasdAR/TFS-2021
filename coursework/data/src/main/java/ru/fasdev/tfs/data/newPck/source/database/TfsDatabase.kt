package ru.fasdev.tfs.data.newPck.source.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.fasdev.tfs.data.newPck.source.database.dao.StreamDao
import ru.fasdev.tfs.data.newPck.source.database.dao.TopicDao
import ru.fasdev.tfs.data.newPck.source.database.model.StreamDb
import ru.fasdev.tfs.data.newPck.source.database.model.TopicDb

@Database(
    entities = [
        StreamDb::class,
        TopicDb::class
    ],
    version = 1
)
abstract class TfsDatabase : RoomDatabase() {
    abstract fun streamDao(): StreamDao
    abstract fun topicDao(): TopicDao
}