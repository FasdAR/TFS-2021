package ru.fasdev.tfs.data.source.database.model

import androidx.room.*

@Entity(
    tableName = "reaction",
    foreignKeys = [
        ForeignKey(
            entity = MessageDb::class,
            parentColumns = ["id"],
            childColumns = ["id_message"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["id_message", "emoji_name"], unique = true)
    ]
)
data class ReactionDb(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    @ColumnInfo(name = "id_message") val idMessage: Long,
    val emoji: String,
    @ColumnInfo(name = "emoji_name") val emojiName: String,
    @ColumnInfo(name = "count_selection") val countSelection: Int,
    @ColumnInfo(name = "is_selected") val isSelected: Boolean
)