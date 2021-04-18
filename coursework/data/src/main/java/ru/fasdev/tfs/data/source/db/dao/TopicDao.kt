package ru.fasdev.tfs.data.source.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import io.reactivex.rxjava3.core.Single
import ru.fasdev.tfs.data.source.db.base.BaseDao
import ru.fasdev.tfs.data.source.db.model.TopicDB

@Dao
abstract class TopicDao: BaseDao<TopicDB>
{
    @Query("SELECT * FROM topic WHERE id_stream = :idStream")
    abstract fun getTopicsInStream(idStream: Long): Single<List<TopicDB>>

    @Query("DELETE FROM topic WHERE id_stream = :idStream")
    abstract fun clearOldData(idStream: Long)

    @Transaction
    open fun insertAndClear(idStream: Long, entity: List<TopicDB>) {
        clearOldData(idStream)
        insert(entity)
    }
}