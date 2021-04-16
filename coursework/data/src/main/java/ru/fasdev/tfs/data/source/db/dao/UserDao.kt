package ru.fasdev.tfs.data.source.db.dao

import androidx.room.Dao
import ru.fasdev.tfs.data.source.db.base.BaseDao
import ru.fasdev.tfs.data.source.db.model.UserDB

@Dao
abstract class UserDao: BaseDao<UserDB>