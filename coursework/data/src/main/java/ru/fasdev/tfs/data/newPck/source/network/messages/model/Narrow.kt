package ru.fasdev.tfs.data.newPck.source.network.messages.model

import kotlinx.serialization.Serializable

@Serializable
class Narrow(
    val operand: String,
    val operator: String
)