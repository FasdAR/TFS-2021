package ru.fasdev.tfs.data.source.network.users.response

import kotlinx.serialization.Serializable
import ru.fasdev.tfs.data.source.network.base.model.ZulipResult
import ru.fasdev.tfs.data.source.network.base.response.BaseResponse
import ru.fasdev.tfs.data.source.network.streams.model.Topic

@Serializable
class UsersMeTopicsResponse(
    override val code: String? = null,
    override val msg: String,
    override val result: ZulipResult,
    val topics: List<Topic>
) : BaseResponse
