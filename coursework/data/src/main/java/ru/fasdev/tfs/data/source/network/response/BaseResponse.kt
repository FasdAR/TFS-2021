package ru.fasdev.tfs.data.source.network.response

import kotlinx.serialization.SerialName

interface BaseResponse
{
    val code: String?
    val msg: String
    val result: Result
}

enum class Result {
    @SerialName("success") SUCCESS,
    @SerialName("error") ERROR
}