package ru.fasdev.tfs.data.newPck.source.network.events.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Operation
{
    @SerialName("remove") REMOVE,
    @SerialName("add") ADD
}