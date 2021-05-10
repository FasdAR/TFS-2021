package ru.fasdev.tfs

import android.app.Application
import android.util.Log
import io.reactivex.plugins.RxJavaPlugins
import retrofit2.Retrofit
import ru.fasdev.tfs.data.source.database.TfsDatabase
import ru.fasdev.tfs.di.module.RetrofitModule
import ru.fasdev.tfs.di.module.RoomModule
import ru.fasdev.tfs.di.provide.ProvideRetrofit


class TfsApp : Application(), ProvideRetrofit {
    object AppComponent {

        val json = RetrofitModule.getJson()
        val retrofit = RetrofitModule.getRetrofit()

        lateinit var newRoomDb: TfsDatabase
        val newStreamDao by lazy { RoomModule.getStreamDao(newRoomDb) }
        val newTopicDao by lazy { RoomModule.getTopicDao(newRoomDb) }

        val newMessagesApi = RetrofitModule.getNewMessageApi(retrofit)
        val newUserApi = RetrofitModule.getNewUserApi(retrofit)
        val newStreamApi = RetrofitModule.getNewStreamApi(retrofit)
        val eventsApi = RetrofitModule.getEventsApi(retrofit)

        val streamDao by lazy { RoomModule.getStreamDao(newRoomDb) }
        val topicDao by lazy { RoomModule.getTopicDao(newRoomDb) }
        val messageDao by lazy { RoomModule.getMessageDao(newRoomDb) }
        val reactionDao by lazy { RoomModule.getReactionDao(newRoomDb) }
        val userDao by lazy { RoomModule.getUserDao(newRoomDb) }
    }

    override fun onCreate() {
        super.onCreate()
        AppComponent.newRoomDb = RoomModule.getNewAppDatabase(this)
        RxJavaPlugins.setErrorHandler { e: Throwable? -> Log.e("RxDisposeError", e?.message.toString())}
    }

    override fun getRetrofit(): Retrofit = AppComponent.retrofit
}
