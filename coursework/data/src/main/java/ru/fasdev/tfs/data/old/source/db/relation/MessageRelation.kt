package ru.fasdev.tfs.data.old.source.db.relation

import androidx.room.Embedded
import androidx.room.Relation
import ru.fasdev.tfs.data.old.source.db.model.MessageDB
import ru.fasdev.tfs.data.old.source.db.model.ReactionDB
import ru.fasdev.tfs.data.old.source.db.model.UserDB

class MessageRelation(
    @Embedded val messageDB: MessageDB,
    @Relation(parentColumn = "id_sender", entityColumn = "id") val sender: UserDB,
    @Relation(parentColumn = "id", entityColumn = "id_message") val reactions: List<ReactionDB>
)