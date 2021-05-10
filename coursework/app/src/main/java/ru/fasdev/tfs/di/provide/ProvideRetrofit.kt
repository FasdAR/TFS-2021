package ru.fasdev.tfs.di.provide

import retrofit2.Retrofit

interface ProvideRetrofit {
    fun getRetrofit(): Retrofit
}
