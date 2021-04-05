package com.example.data.source.network.zulip.users.api

import com.example.data.source.network.zulip.users.response.UserPresenceResponse
import com.example.data.source.network.zulip.users.response.UserResponse
import com.example.data.source.network.zulip.users.response.UsersResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi
{
    @GET("users")
    fun getAllUsers(): Single<UsersResponse>

    @GET("users/{email}/presence")
    fun getUserPresence(@Path("email") email: String): Single<UserPresenceResponse>

    @GET("users/me")
    fun getOwnUser(): Single<UserResponse>
}