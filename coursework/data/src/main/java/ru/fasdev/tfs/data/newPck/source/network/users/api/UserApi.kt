package ru.fasdev.tfs.data.newPck.source.network.users.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import ru.fasdev.tfs.data.newPck.source.network.users.response.UsersMeResponse
import ru.fasdev.tfs.data.newPck.source.network.users.response.UsersPresenceResponse

interface UserApi
{
    @GET("users/me")
    fun getOwnUser(): Single<UsersMeResponse>

    @GET("users/{email}/presence")
    fun getUserPresence(@Path("email") email: String): Single<UsersPresenceResponse>
}