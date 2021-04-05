package ru.fasdev.tfs.view.ui.activity.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.data.source.network.zulip.stream.api.StreamApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.di.ProvideFragmentRouter
import ru.fasdev.tfs.view.feature.util.initEdgeToEdge
import ru.fasdev.tfs.view.ui.global.fragmentRouter.FragmentRouter
import ru.fasdev.tfs.view.ui.global.fragmentRouter.ImplBackPressed
import ru.fasdev.tfs.view.ui.global.fragmentRouter.base.BaseFragmentRouter


class MainActivity : AppCompatActivity(), ProvideFragmentRouter {
    private val fragmentRouter: FragmentRouter by lazy {
        BaseFragmentRouter(supportFragmentManager, R.id.main_container)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initEdgeToEdge()

        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
        val client = OkHttpClient.Builder()
            .addNetworkInterceptor(httpLoggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://tfs-android-2021-spring.zulipchat.com/api/v1/")
            .client(client)
            .addConverterFactory(Json {
                ignoreUnknownKeys = true
            }.asConverterFactory("application/json".toMediaType()))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()

        val service = retrofit.create(StreamApi::class.java)

        service.getStreams("Basic YW5kcmV5cmVkbmlrb3ZAZ21haWwuY29tOk5ud3lUakNVZEpnc0s4Sm1QbWo2YVdIdHJMNWZjUktn")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                Log.d("STREAM_RESULT", response.toString())
            }
    }

    override fun getRouter(): FragmentRouter = fragmentRouter

    override fun onBackPressed() {
        val fragment = fragmentRouter.getCurrentFragment()

        if (fragment is ImplBackPressed) {
            if (fragment.onBackPressed()) return
        }

        super.onBackPressed()
    }
}
