package ru.fasdev.tfs.data.old.source.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "message",
    foreignKeys = [
        ForeignKey(
            entity = UserDB::class,
            parentColumns = ["id"],
            childColumns = ["id_sender"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
class MessageDB(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "id_sender") val idSender: Long,
    val topic: String,
    val text: String,
    val date: Date,
)