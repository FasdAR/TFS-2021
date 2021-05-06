package ru.fasdev.tfs.data.old.source.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stream")
class StreamDB(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "is_sub") val isSub: Boolean,
    val name: String
)