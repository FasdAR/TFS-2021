package ru.fasdev.tfs.data.source.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single
import ru.fasdev.tfs.data.source.database.base.BaseDao
import ru.fasdev.tfs.data.source.database.model.ReactionDb

@Dao
abstract class ReactionDao: BaseDao<ReactionDb>
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertReplace(entity: ReactionDb) : Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertReplace(entity: List<ReactionDb>) : Completable

    @Query("SELECT * FROM reaction WHERE id_message = :idMessage AND emoji_name = :emojiName")
    abstract fun getByReactionByKey(idMessage: Long, emojiName: String): Single<ReactionDb>
}