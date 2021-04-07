package ru.fasdev.tfs.di.module

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import ru.fasdev.tfs.BuildConfig
import ru.fasdev.tfs.data.source.network.users.api.UserApi

class RetrofitModule
{
    companion object {
        const val BASE_URL = "https://tfs-android-2021-spring.zulipchat.com/api/v1/"

        fun getLoggingInterceptor(): HttpLoggingInterceptor {
            return HttpLoggingInterceptor().apply {
                if (BuildConfig.DEBUG) setLevel(HttpLoggingInterceptor.Level.BODY)
                else setLevel(HttpLoggingInterceptor.Level.BASIC)
            }
        }

        fun getOkHttpClient(
            loggingInterceptor: HttpLoggingInterceptor = getLoggingInterceptor()
        ): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
        }

        fun getCallAdapter(): CallAdapter.Factory = RxJava3CallAdapterFactory.create()

        fun getConverterFactory(): Converter.Factory {
            return Json { ignoreUnknownKeys = true }.asConverterFactory("application/json".toMediaType())
        }

        fun getRetrofit(
            okClient: OkHttpClient = getOkHttpClient(),
            callAdapter: CallAdapter.Factory = getCallAdapter(),
            converterFactory: Converter.Factory = getConverterFactory()
        ): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okClient)
                .addCallAdapterFactory(callAdapter)
                .addConverterFactory(converterFactory)
                .build()
        }

        fun getUserApi(retrofit: Retrofit): UserApi {
            return retrofit.create(UserApi::class.java)
        }
    }
}