package ru.fasdev.tfs.data.source.db.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.rxjava3.core.Single
import ru.fasdev.tfs.data.source.db.base.BaseDao
import ru.fasdev.tfs.data.source.db.model.MessageDB
import ru.fasdev.tfs.data.source.db.relation.MessageRelation

@Dao
abstract class MessageDao: BaseDao<MessageDB> {
    @Query("SELECT * FROM message")
    abstract fun getAll(): Single<List<MessageRelation>>
}