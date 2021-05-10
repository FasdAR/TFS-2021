package ru.fasdev.tfs.data.source.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import ru.fasdev.tfs.data.source.database.base.BaseDao
import ru.fasdev.tfs.data.source.database.model.MessageDb
import ru.fasdev.tfs.data.source.database.relation.MessageRelation

@Dao
abstract class MessageDao : BaseDao<MessageDb>
{
    @Query("SELECT * FROM message WHERE topic = :topic")
    abstract fun getMessagesByTopic(topic: String) : Maybe<List<MessageRelation>>

    @Query("SELECT * FROM message WHERE id = :id")
    abstract fun getMessageById(id: Long) : Single<MessageRelation>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertReplace(entity: MessageDb) : Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertReplace(entity: List<MessageDb>) : Completable
}