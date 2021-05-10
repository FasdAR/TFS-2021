package ru.fasdev.tfs.data.source.network.messages.response

import kotlinx.serialization.Serializable
import ru.fasdev.tfs.data.source.network.base.model.ZulipResult
import ru.fasdev.tfs.data.source.network.base.response.BaseResponse
import ru.fasdev.tfs.data.source.network.messages.model.Message

@Serializable
class MessagesResponse (
    override val code: String? = null,
    override val msg: String,
    override val result: ZulipResult,
    val messages: List<Message>
) : BaseResponse