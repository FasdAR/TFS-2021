package ru.fasdev.tfs.data.newPck.source.database.dao

import androidx.room.*
import io.reactivex.Single
import ru.fasdev.tfs.data.newPck.source.database.base.BaseDao
import ru.fasdev.tfs.data.newPck.source.database.model.TopicDb

@Dao
abstract class TopicDao : BaseDao<TopicDb>
{
    @Query("SELECT * FROM topic WHERE id_stream = :idStream")
    abstract fun getStreamTopics(idStream: Long): Single<List<TopicDb>>

    @Query("DELETE FROM topic WHERE id_stream = :idStream")
    abstract fun clearOldData(idStream: Long)

    @Transaction
    open fun insert(entities: List<TopicDb>, idStream: Long) {
        clearOldData(idStream)
        insert(entities)
    }
}