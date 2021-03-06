package ru.fasdev.tfs.data.source.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import io.reactivex.Completable
import io.reactivex.Single
import ru.fasdev.tfs.data.source.database.base.BaseDao
import ru.fasdev.tfs.data.source.database.model.StreamDb

@Dao
abstract class StreamDao : BaseDao<StreamDb> {
    @Query("SELECT * FROM stream WHERE id = :id")
    abstract fun getById(id: Long): Single<StreamDb>

    @Query("SELECT * FROM stream")
    abstract fun getAll(): Single<List<StreamDb>>

    @Query("SELECT * FROM stream WHERE is_sub = :isAmongSubs")
    abstract fun getAll(isAmongSubs: Boolean = false): Single<List<StreamDb>>

    @Query("DELETE FROM stream WHERE is_sub = :isAmongSubs")
    abstract fun clearOldData(isAmongSubs: Boolean): Completable

    @Query("SELECT * FROM stream WHERE name LIKE '%' || :query || '%'")
    abstract fun searchStreams(query: String): Single<List<StreamDb>>

    @Query("SELECT * FROM stream WHERE is_sub = :isAmongSubs AND name LIKE '%' || :query || '%'")
    abstract fun searchStreams(query: String, isAmongSubs: Boolean): Single<List<StreamDb>>

    @Transaction
    open fun insert(entities: List<StreamDb>, isAmongSubs: Boolean) {
        clearOldData(isAmongSubs).subscribe()
        insert(entities).subscribe()
    }
}
