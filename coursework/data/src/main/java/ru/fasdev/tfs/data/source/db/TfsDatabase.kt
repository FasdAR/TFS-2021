package ru.fasdev.tfs.data.source.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.fasdev.tfs.data.source.db.converter.DateConverter
import ru.fasdev.tfs.data.source.db.dao.*
import ru.fasdev.tfs.data.source.db.model.*

@Database(
    entities = [
        StreamDB::class,
        TopicDB::class,
        MessageDB::class,
        UserDB::class,
        ReactionDB::class
    ],
    version = 1
)
@TypeConverters(DateConverter::class)
abstract class TfsDatabase : RoomDatabase()
{
    abstract fun streamDao(): StreamDao
    abstract fun topicDao(): TopicDao
    abstract fun messageDao(): MessageDao
    abstract fun reactionDao(): ReactionDao
    abstract fun userDao(): UserDao
}