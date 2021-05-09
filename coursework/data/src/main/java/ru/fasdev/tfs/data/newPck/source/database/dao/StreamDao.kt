package ru.fasdev.tfs.data.newPck.source.database.dao

import androidx.room.*
import io.reactivex.Single
import ru.fasdev.tfs.data.newPck.source.database.base.BaseDao
import ru.fasdev.tfs.data.newPck.source.database.model.StreamDb

@Dao
abstract class StreamDao: BaseDao<StreamDb>
{
    @Query("SELECT * FROM stream")
    abstract fun getAll(): Single<List<StreamDb>>

    @Query("SELECT * FROM stream WHERE is_sub = :isAmongSubs")
    abstract fun getAll(isAmongSubs: Boolean = false): Single<List<StreamDb>>

    @Query("DELETE FROM stream WHERE is_sub = :isAmongSubs")
    abstract fun clearOldData(isAmongSubs: Boolean)

    @Transaction
    open fun insert(entities: List<StreamDb>, isAmongSubs: Boolean) {
        clearOldData(isAmongSubs)
        insert(entities)
    }
}