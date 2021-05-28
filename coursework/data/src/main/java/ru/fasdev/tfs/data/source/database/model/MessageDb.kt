package ru.fasdev.tfs.data.source.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "message",
    foreignKeys = [
        ForeignKey(
            entity = UserDb::class,
            parentColumns = ["id"],
            childColumns = ["id_sender"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
class MessageDb(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "id_sender") val idSender: Long,
    val topic: String,
    @ColumnInfo(name = "id_stream") val idStream: Long,
    val text: String,
    val date: Date
)
