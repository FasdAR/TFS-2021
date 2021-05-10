package ru.fasdev.tfs.data.source.network.users.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import ru.fasdev.tfs.data.source.network.users.response.UsersIdResponse
import ru.fasdev.tfs.data.source.network.users.response.UsersMeResponse
import ru.fasdev.tfs.data.source.network.users.response.UsersMeSubsResponse
import ru.fasdev.tfs.data.source.network.users.response.UsersMeTopicsResponse
import ru.fasdev.tfs.data.source.network.users.response.UsersPresenceResponse
import ru.fasdev.tfs.data.source.network.users.response.UsersResponse

interface UsersApi {
    @GET("users/me")
    fun getOwnUser(): Single<UsersMeResponse>

    @GET("users/{email}/presence")
    fun getUserPresence(@Path("email") email: String): Single<UsersPresenceResponse>

    @GET("users")
    fun getAllUsers(): Single<UsersResponse>

    @GET("users/{id}")
    fun getUserById(@Path("id") id: Long): Single<UsersIdResponse>

    @GET("users/me/subscriptions")
    fun getOwnSubscriptions(): Single<UsersMeSubsResponse>

    @GET("users/me/{id_stream}/topics")
    fun getOwnTopics(@Path("id_stream") idStream: Long): Single<UsersMeTopicsResponse>
}
