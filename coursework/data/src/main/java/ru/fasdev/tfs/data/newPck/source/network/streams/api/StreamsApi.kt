package ru.fasdev.tfs.data.newPck.source.network.streams.api

import io.reactivex.Single
import retrofit2.http.GET
import ru.fasdev.tfs.data.newPck.source.network.streams.response.StreamsResponse

interface StreamsApi
{
    @GET("streams")
    fun getAllStreams(): Single<StreamsResponse>
}