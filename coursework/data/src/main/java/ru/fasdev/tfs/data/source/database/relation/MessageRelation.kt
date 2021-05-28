package ru.fasdev.tfs.data.source.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import ru.fasdev.tfs.data.source.database.model.MessageDb
import ru.fasdev.tfs.data.source.database.model.ReactionDb
import ru.fasdev.tfs.data.source.database.model.UserDb

class MessageRelation(
    @Embedded val messageDb: MessageDb,
    @Relation(parentColumn = "id_sender", entityColumn = "id") val sender: UserDb,
    @Relation(parentColumn = "id", entityColumn = "id_message") val reactions: List<ReactionDb>
)
