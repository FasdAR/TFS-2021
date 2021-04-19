package ru.fasdev.tfs.data.source.db.dao

import androidx.room.Dao
import ru.fasdev.tfs.data.source.db.base.BaseDao
import ru.fasdev.tfs.data.source.db.model.ReactionDB

@Dao
abstract class ReactionDao: BaseDao<ReactionDB>