package ru.fasdev.tfs.domain.topic.interactor

import io.reactivex.rxjava3.core.Single
import ru.fasdev.tfs.domain.model.Stream
import ru.fasdev.tfs.domain.model.Topic

interface TopicInteractor {
    fun getAllStreams(): Single<List<Stream>>
    fun getAllTopics(): Single<List<Topic>>

    fun getStream(id: Int): Single<Stream>
    fun getTopic(id: Int): Single<Topic>

    fun getStreamInTopic(idTopic: Int): Single<Stream>
    fun getTopicsInStream(idStream: Int): Single<List<Topic>>
    fun searchStream(query: String): Single<List<Stream>>
}
