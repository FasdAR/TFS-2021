package ru.fasdev.tfs.data.newPck.source.network.events.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.fasdev.tfs.data.newPck.source.network.events.model.EventType
import ru.fasdev.tfs.data.newPck.source.network.events.response.EventsResponse
import ru.fasdev.tfs.data.newPck.source.network.events.response.RegisterResponse

interface EventsApi
{
    @POST("register")
    fun registerQueue(
        @Query("event_types") eventTypes: List<EventType>,
        @Query("narrow") narrow: String
    ) : Single<RegisterResponse>

    @GET("events")
    fun events(
        @Query("queue_id") queueId: String,
        @Query("last_event_id") lastEventId: Int,
    ) : Single<EventsResponse>
}