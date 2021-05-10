package ru.fasdev.tfs.data.repository.streams

import io.reactivex.Observable
import io.reactivex.Single
import ru.fasdev.tfs.data.mapper.toStreamDb
import ru.fasdev.tfs.data.mapper.toStreamDomain
import ru.fasdev.tfs.data.mapper.toTopicDb
import ru.fasdev.tfs.data.mapper.toTopicDomain
import ru.fasdev.tfs.data.source.database.dao.StreamDao
import ru.fasdev.tfs.data.source.database.dao.TopicDao
import ru.fasdev.tfs.data.source.database.model.StreamDb
import ru.fasdev.tfs.data.source.network.streams.api.StreamsApi
import ru.fasdev.tfs.data.source.network.users.api.UsersApi
import ru.fasdev.tfs.domain.stream.model.Stream
import ru.fasdev.tfs.domain.stream.model.Topic

typealias StreamData = ru.fasdev.tfs.data.source.network.streams.model.Stream

class StreamsRepositoryImpl(
    private val userApi: UsersApi,
    private val streamApi: StreamsApi,
    private val streamDao: StreamDao,
    private val topicDao: TopicDao
) : StreamsRepository {
    private fun mapStreamsToDomain(list: List<StreamData>): Single<List<Stream>> {
        return Observable.fromIterable(list)
            .map { it.toStreamDomain() }
            .toSortedList { item1, item2 -> item1.name.compareTo(item2.name) }
    }

    private fun mapStreamsDbToDomain(list: List<StreamDb>): Single<List<Stream>> {
        return Observable.fromIterable(list)
            .map { it.toStreamDomain() }
            .toSortedList { item1, item2 -> item1.name.compareTo(item2.name) }
    }

    private fun cacheStreams(list: List<Stream>, isAmongSubs: Boolean) {
        streamDao.insert(
            list.map { it.toStreamDb(isAmongSubs) }
        ).subscribe()
    }

    override fun getAllStreams(): Observable<List<Stream>> {
        val networkSource = streamApi.getAllStreams()
            .flatMap { mapStreamsToDomain(it.streams) }
            .doOnSuccess { cacheStreams(it, false) }
            .toObservable()

        val dbSource = streamDao.getAll()
            .flatMap { mapStreamsDbToDomain(it) }
            .toObservable()

        return dbSource.concatWith(networkSource)
    }

    override fun getOwnSubsStreams(): Observable<List<Stream>> {
        val networkSource = userApi.getOwnSubscriptions()
            .flatMap { mapStreamsToDomain(it.subscriptions) }
            .doOnSuccess { cacheStreams(it, true) }
            .toObservable()

        val dbSource = streamDao.getAll(true)
            .flatMap { mapStreamsDbToDomain(it) }
            .toObservable()
        return dbSource.concatWith(networkSource)
    }

    override fun getOwnTopics(idStream: Long): Observable<List<Topic>> {
        val networkSource = userApi.getOwnTopics(idStream)
            .doOnSuccess {
                val lists = it.topics.withIndex()
                    .map { it.value.toTopicDb(idStream, it.index.toLong()) }
                topicDao.insert(lists, idStream)
            }
            .map {
                it.topics.withIndex().map { it.value.toTopicDomain(idStream, it.index.toLong()) }
                    .sortedBy { it.name }
            }
            .toObservable()

        val dbSource = topicDao.getStreamTopics(idStream)
            .flatMap {
                Observable.fromIterable(it)
                    .map { it.toTopicDomain() }
                    .toSortedList { item1, item2 -> item1.name.compareTo(item2.name) }
            }
            .toObservable()

        return dbSource.concatWith(networkSource)
    }

    override fun searchQuery(query: String, isAmongSubs: Boolean): Single<List<Stream>> {
        val source = if (isAmongSubs) {
            streamDao.searchStreams(query, true)
        } else {
            streamDao.searchStreams(query)
        }

        return source.flatMap { mapStreamsDbToDomain(it) }
    }

    override fun getStreamById(id: Long): Single<Stream> {
        return streamDao.getById(id)
            .map { it.toStreamDomain() }
    }

    override fun getTopicById(id: Long): Single<Topic> {
        return topicDao.getById(id)
            .map { it.toTopicDomain() }
    }
}