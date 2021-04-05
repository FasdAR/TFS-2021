package com.example.data.source.network.zulip.stream.api

import com.example.data.source.network.zulip.stream.response.StreamsResponse
import com.example.data.source.network.zulip.stream.response.SubStreamsResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface StreamApi
{
    @GET("streams")
    fun getAllStreams(): Single<StreamsResponse>

    @GET("users/me/subscriptions")
    fun getSubscriptionsStreams(): Single<SubStreamsResponse>
}