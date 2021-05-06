package ru.fasdev.tfs.data.old.source.network.chat.model

import kotlinx.serialization.Serializable

@Serializable
class FilterNarrow(
    val operand: String,
    val operator: String
)
