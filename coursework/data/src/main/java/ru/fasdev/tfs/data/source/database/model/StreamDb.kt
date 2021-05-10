package ru.fasdev.tfs.data.source.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stream")
class StreamDb (
    @PrimaryKey val id: Long,
    val name: String,
    @ColumnInfo(name = "is_sub") val isSub: Boolean
)