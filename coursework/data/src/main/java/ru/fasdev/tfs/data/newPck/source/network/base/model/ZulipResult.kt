package ru.fasdev.tfs.data.newPck.source.network.base.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ZulipResult
{
    @SerialName("success") SUCCESS,
    @SerialName("error") ERROR
}