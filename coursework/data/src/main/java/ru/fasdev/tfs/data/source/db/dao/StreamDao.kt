package ru.fasdev.tfs.data.source.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import io.reactivex.rxjava3.core.Single
import ru.fasdev.tfs.data.source.db.base.BaseDao
import ru.fasdev.tfs.data.source.db.model.StreamDB

@Dao
abstract class StreamDao: BaseDao<StreamDB>
{
    @Query("SELECT * FROM stream")
    abstract fun getAll(): Single<List<StreamDB>>

    @Query("SELECT * FROM stream WHERE is_sub = 1")
    abstract fun getSubscription(): Single<List<StreamDB>>

    @Query("DELETE FROM stream WHERE is_sub = :isSub")
    abstract fun clearOldData(isSub: Boolean)

    @Query("SELECT * FROM stream WHERE name LIKE '%' || :query || '%'")
    abstract fun searchStream(query: String): Single<List<StreamDB>>

    @Query("SELECT * FROM stream WHERE is_sub = 1 AND name LIKE '%' || :query || '%'")
    abstract fun searchStreamSub(query: String): Single<List<StreamDB>>

    @Transaction
    open fun insertAndClear(entity: List<StreamDB>, isSub: Boolean) {
        clearOldData(isSub)
        insert(entity)
    }
}