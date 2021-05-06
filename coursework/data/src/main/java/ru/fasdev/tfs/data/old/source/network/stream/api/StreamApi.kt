package ru.fasdev.tfs.data.old.source.network.stream.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import ru.fasdev.tfs.data.old.source.network.stream.response.StreamsResponse
import ru.fasdev.tfs.data.old.source.network.stream.response.SubStreamsResponse
import ru.fasdev.tfs.data.old.source.network.stream.response.TopicsResponse

interface StreamApi {
    @GET("streams")
    fun getAllStreams(): Single<StreamsResponse>

    @GET("users/me/subscriptions")
    fun getSubscriptionsStreams(): Single<SubStreamsResponse>

    @GET("users/me/{id_stream}/topics")
    fun getTopics(@Path("id_stream") idStream: Long): Single<TopicsResponse>
}
