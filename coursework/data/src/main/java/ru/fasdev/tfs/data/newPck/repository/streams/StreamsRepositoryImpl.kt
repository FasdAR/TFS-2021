package ru.fasdev.tfs.data.newPck.repository.streams

import io.reactivex.Observable
import io.reactivex.Single
import ru.fasdev.tfs.data.newPck.mapper.toStreamDb
import ru.fasdev.tfs.data.newPck.mapper.toStreamDomain
import ru.fasdev.tfs.data.newPck.mapper.toTopicDb
import ru.fasdev.tfs.data.newPck.mapper.toTopicDomain
import ru.fasdev.tfs.data.newPck.source.database.TfsDatabase
import ru.fasdev.tfs.data.newPck.source.database.dao.StreamDao
import ru.fasdev.tfs.data.newPck.source.database.dao.TopicDao
import ru.fasdev.tfs.data.newPck.source.network.streams.api.StreamsApi
import ru.fasdev.tfs.data.newPck.source.network.users.api.UsersApi
import ru.fasdev.tfs.domain.newPck.stream.model.Stream
import ru.fasdev.tfs.domain.newPck.stream.model.Topic

class StreamsRepositoryImpl(
    private val userApi: UsersApi,
    private val streamApi: StreamsApi,
    private val tfsDatabase: TfsDatabase
) : StreamsRepository {
    private val streamDao: StreamDao get() = tfsDatabase.streamDao()
    private val topicDao: TopicDao get() = tfsDatabase.topicDao()

    override fun getAllStreams(): Observable<List<Stream>> {
        val networkSource = streamApi.getAllStreams()
            .doOnSuccess {
                val lists = it.streams.map { it.toStreamDb(false) }
                streamDao.insert(lists, false)
            }
            .flatMap {
                Observable.fromIterable(it.streams)
                    .map { it.toStreamDomain() }
                    .toSortedList { item1, item2 -> item1.name.compareTo(item2.name) }
            }
            .toObservable()

        val dbSource = streamDao.getAll()
            .flatMap {
                Observable.fromIterable(it)
                    .map { it.toStreamDomain() }
                    .toSortedList { item1, item2 -> item1.name.compareTo(item2.name) }
            }
            .toObservable()

        return dbSource.concatWith(networkSource)
    }

    override fun getOwnSubsStreams(): Observable<List<Stream>> {
        val networkSource = userApi.getOwnSubscriptions()
            .doOnSuccess {
                val lists = it.subscriptions.map { it.toStreamDb(true) }
                streamDao.insert(lists, true)
            }
            .flatMap {
                Observable.fromIterable(it.subscriptions)
                    .map { it.toStreamDomain() }
                    .toSortedList { item1, item2 -> item1.name.compareTo(item2.name) }
            }
            .toObservable()

        val dbSource = streamDao.getAll(true)
            .flatMap {
                Observable.fromIterable(it)
                    .map { it.toStreamDomain() }
                    .toSortedList { item1, item2 -> item1.name.compareTo(item2.name) }
            }
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
        //TODO: SEARCH IN DATA BASE
        return Single.just(listOf())
    }
}