package ru.fasdev.tfs

import android.app.Application
import retrofit2.Retrofit
import ru.fasdev.tfs.data.source.network.users.api.UserApi
import ru.fasdev.tfs.di.module.RetrofitModule
import ru.fasdev.tfs.di.provide.ProvideRetrofit

class TfsApp : Application(), ProvideRetrofit
{
    object AppComponent {
        val retrofit = RetrofitModule.getRetrofit()
        val userApi = RetrofitModule.getUserApi(retrofit)
    }

    override fun getRetrofit(): Retrofit = AppComponent.retrofit
    override fun getUserApi(): UserApi = AppComponent.userApi
}