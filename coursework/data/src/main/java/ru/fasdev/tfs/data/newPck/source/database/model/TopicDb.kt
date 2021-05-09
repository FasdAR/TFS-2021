package ru.fasdev.tfs.data.newPck.source.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "topic",
    foreignKeys = [
        ForeignKey(
            entity = StreamDb::class,
            parentColumns = ["id"],
            childColumns = ["id_stream"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
class TopicDb(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "id_stream") val streamId: Long,
    val name: String
)