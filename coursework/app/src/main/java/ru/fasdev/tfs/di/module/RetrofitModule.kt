package ru.fasdev.tfs.di.module

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import ru.fasdev.tfs.BuildConfig
import ru.fasdev.tfs.data.source.network.chat.api.ChatApi
import ru.fasdev.tfs.data.source.network.stream.api.StreamApi
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

        fun getAuthInterceptor(): Interceptor = Interceptor { chain ->
            val token = "Basic YW5kcmV5cmVkbmlrb3ZAZ21haWwuY29tOk5ud3lUakNVZEpnc0s4Sm1QbWo2YVdIdHJMNWZjUktn"
            val request: Request = chain.request()
            val authRequest: Request = request.newBuilder()
                .header("Authorization", token).build()

            return@Interceptor chain.proceed(authRequest)
        }

        fun getOkHttpClient(
            loggingInterceptor: HttpLoggingInterceptor = getLoggingInterceptor(),
            authInterceptor: Interceptor? = getAuthInterceptor()
        ): OkHttpClient {
            return OkHttpClient.Builder()
                .apply {
                    addInterceptor(loggingInterceptor)
                    authInterceptor?.let { addInterceptor(it) }
                }.build()
        }

        fun getCallAdapter(): CallAdapter.Factory = RxJava3CallAdapterFactory.create()

        fun getJson(): Json {
            return Json { ignoreUnknownKeys = true }
        }

        fun getConverterFactory(json: Json = getJson()): Converter.Factory {
            return json.asConverterFactory("application/json".toMediaType())
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

        fun getStreamApi(retrofit: Retrofit): StreamApi {
            return retrofit.create(StreamApi::class.java)
        }

        fun getChatApi(retrofit: Retrofit): ChatApi {
            return retrofit.create(ChatApi::class.java)
        }
    }
}