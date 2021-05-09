package ru.fasdev.tfs.data.newPck.source.database.base

import androidx.room.*

@Dao
interface BaseDao<T>
{
    @Update
    fun update(entity: T)

    @Update
    fun update(entity: List<T>)

    @Delete
    fun delete(entity: T)

    @Delete
    fun delete(entity: List<T>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: T)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: List<T>)
}
