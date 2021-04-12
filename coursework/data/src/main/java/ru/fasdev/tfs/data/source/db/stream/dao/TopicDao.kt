package ru.fasdev.tfs.data.source.db.stream.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.fasdev.tfs.data.source.db.stream.model.TopicDB

@Dao
interface TopicDao
{
    @Query("SELECT * FROM topic WHERE id_stream = :idStream")
    fun getTopicsInStream(idStream: Long): Single<List<TopicDB>>

    @Query("DELETE FROM topic")
    fun dropTable(): Completable
}