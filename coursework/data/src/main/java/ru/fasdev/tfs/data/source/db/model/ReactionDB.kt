package ru.fasdev.tfs.data.source.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "reaction",
    foreignKeys = [
        ForeignKey(
            entity = MessageDB::class,
            parentColumns = ["id"],
            childColumns = ["id_message"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
class ReactionDB(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    @ColumnInfo(name = "id_message") val idMessage: Long,
    val emoji: String,
    @ColumnInfo(name = "emoji_name") val emojiName: String,
    @ColumnInfo(name = "count_selection") val countSelection: Int,
    @ColumnInfo(name = "is_selected") val isSelected: Boolean
)