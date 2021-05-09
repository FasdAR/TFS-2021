package ru.fasdev.tfs

import android.app.Application
import android.util.Log
import io.reactivex.plugins.RxJavaPlugins
import retrofit2.Retrofit
import ru.fasdev.tfs.data.old.source.db.TfsDatabase
import ru.fasdev.tfs.data.old.source.db.dao.StreamDao
import ru.fasdev.tfs.data.old.source.db.dao.TopicDao
import ru.fasdev.tfs.data.old.source.network.users.api.UserApi
import ru.fasdev.tfs.di.module.RetrofitModule
import ru.fasdev.tfs.di.module.RoomModule
import ru.fasdev.tfs.di.provide.ProvideRetrofit
import ru.fasdev.tfs.di.provide.ProvideRoom


class TfsApp : Application(), ProvideRetrofit, ProvideRoom {
    object AppComponent {

        val json = RetrofitModule.getJson()
        val retrofit = RetrofitModule.getRetrofit()
        val userApi = RetrofitModule.getUserApi(retrofit)
        val streamApi = RetrofitModule.getStreamApi(retrofit)
        val chatApi = RetrofitModule.getChatApi(retrofit)

        val newUserApi = RetrofitModule.getNewUserApi(retrofit)
        val newStreamApi = RetrofitModule.getNewStreamApi(retrofit)

        lateinit var roomDb: TfsDatabase
        val streamDao by lazy { RoomModule.getStreamDao(roomDb) }
        val topicDao by lazy { RoomModule.getTopicDao(roomDb) }
        val messageDao by lazy { RoomModule.getMessageDao(roomDb) }
        val reactionDao by lazy { RoomModule.getReactionDao(roomDb) }
        val userDao by lazy { RoomModule.getUserDao(roomDb) }
    }

    override fun onCreate() {
        super.onCreate()
        AppComponent.roomDb = RoomModule.getAppDatabase(this)
        RxJavaPlugins.setErrorHandler { e: Throwable? -> Log.e("RxDisposeError", e?.message.toString())}
    }

    override fun getRetrofit(): Retrofit = AppComponent.retrofit
    override fun getUserApi(): UserApi = AppComponent.userApi

    override fun getStreamDao(): StreamDao = AppComponent.streamDao
    override fun getTopicDao(): TopicDao = AppComponent.topicDao
}
