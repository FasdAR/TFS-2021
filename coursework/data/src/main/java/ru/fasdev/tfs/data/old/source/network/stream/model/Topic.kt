package ru.fasdev.tfs.data.old.source.network.stream.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Topic(val name: String, @SerialName("max_id") val maxId: Int)
