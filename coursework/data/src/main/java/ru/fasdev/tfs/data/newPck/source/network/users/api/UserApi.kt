package ru.fasdev.tfs.data.newPck.source.network.users.api

import io.reactivex.Maybe
import io.reactivex.Single
import retrofit2.http.GET
import ru.fasdev.tfs.data.newPck.source.network.users.response.OwnUserResponse

interface UserApi
{
    @GET("users/me")
    fun getOwnUser(): Single<OwnUserResponse>
}