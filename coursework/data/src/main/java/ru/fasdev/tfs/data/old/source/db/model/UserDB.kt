package ru.fasdev.tfs.data.old.source.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class UserDB (
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "avatar_url") val avatarUrl: String,
    @ColumnInfo(name = "full_name") val fullName: String,
    val email: String
)