package ru.fasdev.tfs.data.source.db.stream.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.fasdev.tfs.data.source.db.base.BaseDao
import ru.fasdev.tfs.data.source.db.stream.model.StreamDB

@Dao
interface StreamDao: BaseDao<StreamDB>
{
    @Query("SELECT * FROM stream")
    fun getAll(): Single<List<StreamDB>>

    @Query("SELECT * FROM stream WHERE is_sub = 1")
    fun getSubscription(): Single<List<StreamDB>>

    @Query("DELETE FROM stream")
    fun dropTable(): Completable
}