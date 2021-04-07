package ru.fasdev.tfs.data.source.network.users.api

import ru.fasdev.tfs.data.source.network.users.response.UserPresenceResponse
import ru.fasdev.tfs.data.source.network.users.response.OwnUserResponse
import ru.fasdev.tfs.data.source.network.users.response.AllUsersResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import ru.fasdev.tfs.data.source.network.users.response.UserResponse

interface UserApi
{
    @GET("users")
    fun getAllUsers(): Single<AllUsersResponse>

    @GET("users/{email}/presence")
    fun getUserPresence(@Path("email") email: String): Single<UserPresenceResponse>

    @GET("users/me")
    fun getOwnUser(): Single<OwnUserResponse>

    @GET("users/{id}")
    fun getUserById(@Path("id") id: Long): Single<UserResponse>
}