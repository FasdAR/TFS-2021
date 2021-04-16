package ru.fasdev.tfs.data.source.db.dao

import androidx.room.*
import io.reactivex.rxjava3.core.Single
import ru.fasdev.tfs.data.source.db.base.BaseDao
import ru.fasdev.tfs.data.source.db.model.MessageDB
import ru.fasdev.tfs.data.source.db.model.ReactionDB
import ru.fasdev.tfs.data.source.db.model.UserDB
import ru.fasdev.tfs.data.source.db.relation.MessageRelation

@Dao
abstract class MessageDao: BaseDao<MessageDB> {
    @Query("SELECT * FROM message WHERE topic = :topic")
    abstract fun getAllByTopic(topic: String): Single<List<MessageRelation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertUser(entity: UserDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertReactions(entity: List<ReactionDB>)

    @Query("SELECT COUNT(id) FROM message WHERE topic = :topic")
    abstract fun getDataCount(topic: String): Int

    @Query("DELETE FROM message WHERE id IN (SELECT id FROM message ORDER BY date DESC LIMIT :numberDelete)")
    abstract fun deleteLastRow(numberDelete: Int)

    @Transaction
    open fun transactionInsertMessage(
        messageDB: MessageDB,
        userDB: UserDB,
        reactions: List<ReactionDB>
    ) {
        insertUser(userDB)
        insert(messageDB)
        if (reactions.isNotEmpty()) insertReactions(reactions)
    }
}