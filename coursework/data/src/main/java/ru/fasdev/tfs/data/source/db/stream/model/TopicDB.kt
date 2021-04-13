package ru.fasdev.tfs.data.source.db.stream.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "topic",
    foreignKeys = [
        ForeignKey(
            entity = StreamDB::class,
            parentColumns = ["id"],
            childColumns = ["id_stream"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
class TopicDB(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "id_stream") val streamId: Long,
    val name: String
)