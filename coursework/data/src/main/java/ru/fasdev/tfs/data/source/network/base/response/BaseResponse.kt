package ru.fasdev.tfs.data.source.network.base.response

import ru.fasdev.tfs.data.source.network.base.model.ZulipResult

interface BaseResponse {
    val code: String?
    val msg: String
    val result: ZulipResult
}