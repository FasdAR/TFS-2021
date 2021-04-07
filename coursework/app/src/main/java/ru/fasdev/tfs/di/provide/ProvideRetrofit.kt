package ru.fasdev.tfs.di.provide

import retrofit2.Retrofit
import ru.fasdev.tfs.data.source.network.users.api.UserApi

interface ProvideRetrofit
{
    fun getRetrofit(): Retrofit
    fun getUserApi(): UserApi
}