package ru.fasdev.tfs.data.source.network.stream.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Topic (val name: String, @SerialName("max_id") val maxId: Int)