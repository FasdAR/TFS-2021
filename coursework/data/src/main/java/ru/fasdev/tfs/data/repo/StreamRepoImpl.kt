package ru.fasdev.tfs.data.repo

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable.fromIterable
import io.reactivex.rxjava3.core.Single
import ru.fasdev.tfs.data.mapper.toConstHash
import ru.fasdev.tfs.data.mapper.toStreamDomain
import ru.fasdev.tfs.data.mapper.toTopicDomain
import ru.fasdev.tfs.data.source.db.stream.dao.StreamDao
import ru.fasdev.tfs.data.source.db.stream.dao.TopicDao
import ru.fasdev.tfs.data.source.db.stream.model.StreamDB
import ru.fasdev.tfs.data.source.db.stream.model.TopicDB
import ru.fasdev.tfs.data.source.network.stream.api.StreamApi
import ru.fasdev.tfs.domain.stream.model.Stream
import ru.fasdev.tfs.domain.stream.model.Topic
import ru.fasdev.tfs.domain.stream.repo.StreamRepo

typealias NetworkStream = ru.fasdev.tfs.data.source.network.stream.model.Stream

class StreamRepoImpl(
    private val streamApi: StreamApi,
    private val streamDao: StreamDao,
    private val topicDao: TopicDao
) : StreamRepo {
    private fun Single<List<NetworkStream>>.mapToDomain(): Single<List<Stream>> {
        return flatMapObservable(::fromIterable)
            .map { it.toStreamDomain() }
            .toSortedList { o1, o2 -> o1.name.compareTo(o2.name) }
    }

    @JvmName("mapToDomainStreamDB")
    private fun Single<List<StreamDB>>.mapToDomain(): Single<List<Stream>> {
        return flatMapObservable(::fromIterable)
            .map { it.toStreamDomain() }
            .toSortedList { o1, o2 -> o1.name.compareTo(o2.name) }
    }

    private fun Single<List<Stream>>.cacheToDB(isSub: Boolean = false): Single<List<Stream>> {
        return doOnSuccess { streams ->
            val streamsDB = streams.map { stream ->
                StreamDB(id = stream.id, isSub, stream.name)
            }

            streamDao.insertAndClear(streamsDB, isSub)
        }
    }

    override fun getAllStreams(): Flowable<List<Stream>> {
        val dbSource = streamDao.getAll()
            .mapToDomain()

        val networkSource = streamApi.getAllStreams()
            .map { it.streams }
            .mapToDomain()
            .cacheToDB()

        return dbSource.concatWith(networkSource)
    }

    override fun getSubStreams(): Flowable<List<Stream>> {
        val dbSource = streamDao.getSubscription()
            .mapToDomain()

        val networkSource = streamApi.getSubscriptionsStreams()
            .map { it.subscriptions }
            .mapToDomain()
            .cacheToDB(isSub = true)

        return dbSource.concatWith(networkSource)
    }

    override fun getTopics(idStream: Long): Flowable<List<Topic>> {
        val dbSource = topicDao.getTopicsInStream(idStream)
            .flatMapObservable(::fromIterable)
            .map { topic ->
                val generateId = topic.name.toConstHash().toLong() + idStream
                Topic(generateId, topic.name)
            }
            .toSortedList { o1, o2 -> o1.name.compareTo(o2.name) }

        val networkSource = streamApi.getTopics(idStream)
            .map { it.topics }
            .flatMapObservable(::fromIterable)
            .map { it.toTopicDomain(idStream) }
            .toSortedList { o1, o2 -> o1.name.compareTo(o2.name) }
            .doOnSuccess { topics ->
                val topicsDb = topics.map { topic ->
                    TopicDB(id = topic.id, streamId = idStream, name = topic.name)
                }

                topicDao.insertAndClear(idStream, topicsDb)
            }

        return dbSource.concatWith(networkSource)
    }

    override fun searchQuery(isSub: Boolean, query: String): Single<List<Stream>> {
        val source = if (query.isEmpty()) {
            if (isSub) {
                streamDao.getSubscription()
            } else {
                streamDao.getAll()
            }
        } else {
            if (isSub) {
                streamDao.searchStreamSub(query)
            } else {
                streamDao.searchStream(query)
            }
        }

        return source
            .flatMapObservable(::fromIterable)
            .map { it.toStreamDomain() }
            .toSortedList { o1, o2 -> o1.name.compareTo(o2.name) }
    }
}
