package ru.fasdev.tfs.di.provide

import ru.fasdev.tfs.data.old.source.db.dao.StreamDao
import ru.fasdev.tfs.data.old.source.db.dao.TopicDao

interface ProvideRoom {
    fun getStreamDao(): StreamDao
    fun getTopicDao(): TopicDao
}
