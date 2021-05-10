package ru.fasdev.tfs.data.source.database.base

import androidx.room.*
import io.reactivex.Completable

@Dao
interface BaseDao<T>
{
    @Update
    fun update(entity: T) : Completable

    @Update
    fun update(entity: List<T>) : Completable

    @Delete
    fun delete(entity: T) : Completable

    @Delete
    fun delete(entity: List<T>) : Completable

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: T) : Completable

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: List<T>) : Completable
}
