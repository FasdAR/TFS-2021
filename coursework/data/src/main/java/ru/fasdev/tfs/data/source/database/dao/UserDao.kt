package ru.fasdev.tfs.data.source.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import io.reactivex.Completable
import ru.fasdev.tfs.data.source.database.base.BaseDao
import ru.fasdev.tfs.data.source.database.model.UserDb

@Dao
abstract class UserDao: BaseDao<UserDb>
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertReplace(entity: UserDb) : Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertReplace(entity: List<UserDb>) : Completable
}