package ru.fasdev.tfs.data.source.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.fasdev.tfs.data.source.database.base.converter.DateConverter
import ru.fasdev.tfs.data.source.database.dao.*
import ru.fasdev.tfs.data.source.database.model.*

@Database(
    entities = [
        StreamDb::class,
        TopicDb::class,
        MessageDb::class,
        ReactionDb::class,
        UserDb::class
    ],
    version = 1
)
@TypeConverters(DateConverter::class)
abstract class TfsDatabase : RoomDatabase() {
    abstract fun streamDao(): StreamDao
    abstract fun topicDao(): TopicDao
    abstract fun messageDao(): MessageDao
    abstract fun reactionDao(): ReactionDao
    abstract fun userDao(): UserDao
}