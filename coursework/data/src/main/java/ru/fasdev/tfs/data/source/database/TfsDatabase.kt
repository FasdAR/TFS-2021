package ru.fasdev.tfs.data.source.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.fasdev.tfs.data.source.database.base.converter.DateConverter
import ru.fasdev.tfs.data.source.database.dao.MessageDao
import ru.fasdev.tfs.data.source.database.dao.ReactionDao
import ru.fasdev.tfs.data.source.database.dao.StreamDao
import ru.fasdev.tfs.data.source.database.dao.TopicDao
import ru.fasdev.tfs.data.source.database.dao.UserDao
import ru.fasdev.tfs.data.source.database.model.MessageDb
import ru.fasdev.tfs.data.source.database.model.ReactionDb
import ru.fasdev.tfs.data.source.database.model.StreamDb
import ru.fasdev.tfs.data.source.database.model.TopicDb
import ru.fasdev.tfs.data.source.database.model.UserDb

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
