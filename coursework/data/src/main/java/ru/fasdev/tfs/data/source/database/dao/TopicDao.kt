package ru.fasdev.tfs.data.source.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import io.reactivex.Completable
import io.reactivex.Single
import ru.fasdev.tfs.data.source.database.base.BaseDao
import ru.fasdev.tfs.data.source.database.model.TopicDb

@Dao
abstract class TopicDao : BaseDao<TopicDb> {
    @Query("SELECT * FROM topic WHERE id = :id")
    abstract fun getById(id: Long): Single<TopicDb>

    @Query("SELECT * FROM topic WHERE id_stream = :idStream")
    abstract fun getStreamTopics(idStream: Long): Single<List<TopicDb>>

    @Query("DELETE FROM topic WHERE id_stream = :idStream")
    abstract fun clearOldData(idStream: Long): Completable

    @Transaction
    open fun insert(entities: List<TopicDb>, idStream: Long) {
        clearOldData(idStream).subscribe()
        insert(entities).subscribe()
    }
}
