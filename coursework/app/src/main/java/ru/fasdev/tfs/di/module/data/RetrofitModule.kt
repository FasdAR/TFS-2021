package ru.fasdev.tfs.di.module.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import ru.fasdev.tfs.BuildConfig
import ru.fasdev.tfs.data.source.network.events.api.EventsApi
import ru.fasdev.tfs.data.source.network.messages.api.MessagesApi
import ru.fasdev.tfs.data.source.network.streams.api.StreamsApi
import ru.fasdev.tfs.data.source.network.users.api.UsersApi
import ru.fasdev.tfs.di.scope.AppScope
import java.util.concurrent.TimeUnit

@Module
class RetrofitModule {
    private companion object {
        private const val BASE_URL = "https://tfs-android-2021-spring.zulipchat.com/api/v1/"
        private const val TOKEN_AUTH = "Basic YW5kcmV5cmVkbmlrb3ZAZ21haWwuY29tOk5ud3lUakNVZEpnc0s4Sm1QbWo2YVdIdHJMNWZjUktn"
        private const val HEADER_AUTH = "Authorization"
        private const val TIME_OUT = 60L

        const val JSON_MEDIA_TYPE = "application/json"
    }

    @Provides
    @AppScope
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                setLevel(HttpLoggingInterceptor.Level.BASIC)
            }
        }
    }

    @Provides
    @AppScope
    fun provideAuthInterceptor(): Interceptor = Interceptor { chain ->
        val token = TOKEN_AUTH
        val request: Request = chain.request()
        val authRequest: Request = request.newBuilder()
            .header(HEADER_AUTH, token).build()

        return@Interceptor chain.proceed(authRequest)
    }

    @Provides
    @AppScope
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .apply {
                readTimeout(TIME_OUT, TimeUnit.SECONDS)
                connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                addInterceptor(loggingInterceptor)
                addInterceptor(authInterceptor)
            }.build()
    }

    @Provides
    @AppScope
    fun provideCallAdapter(): CallAdapter.Factory = RxJava2CallAdapterFactory.create()

    @Provides
    @AppScope
    fun provideJson(): Json = Json { ignoreUnknownKeys = true }

    @Provides
    @AppScope
    fun provideConverterFactory(json: Json): Converter.Factory {
        return json.asConverterFactory(JSON_MEDIA_TYPE.toMediaType())
    }

    @Provides
    @AppScope
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        callAdapter: CallAdapter.Factory,
        converterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(callAdapter)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    @AppScope
    fun provideMessagesApi(retrofit: Retrofit): MessagesApi {
        return retrofit.create(MessagesApi::class.java)
    }

    @Provides
    @AppScope
    fun provideUsesApi(retrofit: Retrofit): UsersApi {
        return retrofit.create(UsersApi::class.java)
    }

    @Provides
    @AppScope
    fun provideStreamsApi(retrofit: Retrofit): StreamsApi {
        return retrofit.create(StreamsApi::class.java)
    }

    @Provides
    @AppScope
    fun provideEventsApi(retrofit: Retrofit): EventsApi {
        return retrofit.create(EventsApi::class.java)
    }
}
