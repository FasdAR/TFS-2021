package ru.fasdev.tfs.data.source.network.chat.response

import kotlinx.serialization.Serializable
import ru.fasdev.tfs.data.source.network.base.response.BaseResponse
import ru.fasdev.tfs.data.source.network.base.response.Result
import ru.fasdev.tfs.data.source.network.chat.model.Message

@Serializable
class AllMessagesResponse (
    override val code: String? = null,
    override val msg: String,
    override val result: Result,
    val messages: List<Message>
) : BaseResponse