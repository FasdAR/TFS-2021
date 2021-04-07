package ru.fasdev.tfs.data.source.network.stream.api

import ru.fasdev.tfs.data.source.network.stream.response.StreamsResponse
import ru.fasdev.tfs.data.source.network.stream.response.SubStreamsResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface StreamApi
{
    @GET("streams")
    fun getAllStreams(): Single<StreamsResponse>

    @GET("users/me/subscriptions")
    fun getSubscriptionsStreams(): Single<SubStreamsResponse>
}