package ru.fasdev.tfs.di.provide

import retrofit2.Retrofit
import ru.fasdev.tfs.data.source.db.stream.dao.StreamDao
import ru.fasdev.tfs.data.source.db.stream.dao.TopicDao
import ru.fasdev.tfs.data.source.network.users.api.UserApi

interface ProvideRoom {
    fun getStreamDao(): StreamDao
    fun getTopicDao(): TopicDao
}
