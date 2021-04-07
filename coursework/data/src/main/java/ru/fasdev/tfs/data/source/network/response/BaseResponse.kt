package ru.fasdev.tfs.data.source.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface BaseResponse
{
    val code: String?
    val msg: String
    val result: Result
}

@Serializable
enum class Result {
    @SerialName("success") SUCCESS,
    @SerialName("error") ERROR
}