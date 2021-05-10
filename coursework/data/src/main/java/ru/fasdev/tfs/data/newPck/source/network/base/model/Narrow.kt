package ru.fasdev.tfs.data.newPck.source.network.base.model

import kotlinx.serialization.Serializable

@Serializable
class Narrow(
    val operand: String,
    val operator: String
)