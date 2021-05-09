package ru.fasdev.tfs.data.newPck.source.network.users.response

import kotlinx.serialization.Serializable
import ru.fasdev.tfs.data.newPck.source.network.base.model.ZulipResult
import ru.fasdev.tfs.data.newPck.source.network.base.response.BaseResponse
import ru.fasdev.tfs.data.newPck.source.network.streams.model.Stream

@Serializable
class UsersMeSubsResponse(
    override val code: String? = null,
    override val msg: String,
    override val result: ZulipResult,
    val subscriptions: List<Stream>
) : BaseResponse