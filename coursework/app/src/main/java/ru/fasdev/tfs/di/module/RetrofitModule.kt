package ru.fasdev.tfs.di.module

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
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
import ru.fasdev.tfs.data.newPck.source.network.messages.api.MessagesApi
import ru.fasdev.tfs.data.old.source.network.chat.api.ChatApi
import ru.fasdev.tfs.data.old.source.network.stream.api.StreamApi
import ru.fasdev.tfs.data.old.source.network.users.api.UserApi

class RetrofitModule {
    companion object {
        private const val BASE_URL = "https://tfs-android-2021-spring.zulipchat.com/api/v1/"
        private const val TOKEN_AUTH = "Basic YW5kcmV5cmVkbmlrb3ZAZ21haWwuY29tOk5ud3lUakNVZEpnc0s4Sm1QbWo2YVdIdHJMNWZjUktn"
        private const val HEADER_AUTH = "Authorization"
        private const val JSON_MEDIA_TYPE = "application/json"

        private fun getLoggingInterceptor(): HttpLoggingInterceptor {
            return HttpLoggingInterceptor().apply {
                if (BuildConfig.DEBUG) {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                }
                else {
                    setLevel(HttpLoggingInterceptor.Level.BASIC)
                }
            }
        }

        private fun getAuthInterceptor(): Interceptor = Interceptor { chain ->
            val token = TOKEN_AUTH
            val request: Request = chain.request()
            val authRequest: Request = request.newBuilder()
                .header(HEADER_AUTH, token).build()

            return@Interceptor chain.proceed(authRequest)
        }

        private fun getOkHttpClient(
            loggingInterceptor: HttpLoggingInterceptor = getLoggingInterceptor(),
            authInterceptor: Interceptor? = getAuthInterceptor()
        ): OkHttpClient {
            return OkHttpClient.Builder()
                .apply {
                    addInterceptor(loggingInterceptor)
                    authInterceptor?.let { addInterceptor(it) }
                }.build()
        }

        private fun getCallAdapter(): CallAdapter.Factory = RxJava2CallAdapterFactory.create()

        fun getJson(): Json {
            return Json { ignoreUnknownKeys = true }
        }

        private fun getConverterFactory(json: Json = getJson()): Converter.Factory {
            return json.asConverterFactory(JSON_MEDIA_TYPE.toMediaType())
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

        fun getNewMessageApi(retrofit: Retrofit): MessagesApi {
            return retrofit.create(MessagesApi::class.java)
        }

        fun getNewUserApi(retrofit: Retrofit): ru.fasdev.tfs.data.newPck.source.network.users.api.UsersApi {
            return retrofit.create(ru.fasdev.tfs.data.newPck.source.network.users.api.UsersApi::class.java)
        }

        fun getNewStreamApi(retrofit: Retrofit): ru.fasdev.tfs.data.newPck.source.network.streams.api.StreamsApi {
            return retrofit.create(ru.fasdev.tfs.data.newPck.source.network.streams.api.StreamsApi::class.java)
        }

        fun getStreamApi(retrofit: Retrofit): StreamApi {
            return retrofit.create(StreamApi::class.java)
        }

        fun getChatApi(retrofit: Retrofit): ChatApi {
            return retrofit.create(ChatApi::class.java)
        }
    }
}
