package com.example.data.source.network.zulip.response

import kotlinx.serialization.SerialName

interface SharedResponse
{
    val code: String?
    val msg: String
    val result: Result
}

enum class Result {
    @SerialName("success") SUCCESS,
    @SerialName("error") ERROR
}