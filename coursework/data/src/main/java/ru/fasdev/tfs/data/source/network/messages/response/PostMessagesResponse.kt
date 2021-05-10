package ru.fasdev.tfs.data.source.network.messages.response

import kotlinx.serialization.Serializable
import ru.fasdev.tfs.data.source.network.base.model.ZulipResult
import ru.fasdev.tfs.data.source.network.base.response.BaseResponse

@Serializable
class PostMessagesResponse(
    override val code: String? = null,
    override val msg: String,
    override val result: ZulipResult,
    val id: Long
) : BaseResponse
