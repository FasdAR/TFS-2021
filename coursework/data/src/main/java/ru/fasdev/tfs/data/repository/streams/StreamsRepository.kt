package ru.fasdev.tfs.data.repository.streams

import io.reactivex.Observable
import io.reactivex.Single
import ru.fasdev.tfs.domain.stream.model.Stream
import ru.fasdev.tfs.domain.stream.model.Topic

interface StreamsRepository {
    fun getAllStreams(): Observable<List<Stream>>
    fun getOwnSubsStreams(): Observable<List<Stream>>
    fun getOwnTopics(idStream: Long): Observable<List<Topic>>
    fun searchQuery(query: String, isAmongSubs: Boolean = false): Single<List<Stream>>
    fun getStreamById(id: Long): Single<Stream>
    fun getTopicById(id: Long): Single<Topic>
}