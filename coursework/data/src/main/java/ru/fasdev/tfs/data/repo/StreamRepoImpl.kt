package ru.fasdev.tfs.data.repo

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable.fromIterable
import io.reactivex.rxjava3.core.Single
import ru.fasdev.tfs.data.mapper.toStreamDomain
import ru.fasdev.tfs.data.mapper.toTopicDomain
import ru.fasdev.tfs.data.source.db.stream.dao.StreamDao
import ru.fasdev.tfs.data.source.db.stream.dao.TopicDao
import ru.fasdev.tfs.data.source.db.stream.model.StreamDB
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
            .toList()
    }

    @JvmName("mapToDomainStreamDB")
    private fun Single<List<StreamDB>>.mapToDomain(): Single<List<Stream>> {
        return flatMapObservable(::fromIterable)
            .map { it.toStreamDomain() }
            .toList()
    }

    private fun Single<List<Stream>>.cacheToDB(isSub: Boolean = false): Single<List<Stream>> {
        return doOnSuccess { streams ->
            val streamsDB = streams.map { stream ->
                StreamDB(id = stream.id, isSub, stream.name)
            }

            streamDao.dropTable()
            streamDao.insert(streamsDB)
        }
    }

    override fun getAllStreams(): Flowable<List<Stream>> {
        val dbSource = streamDao.getAll()
            .mapToDomain()

        val newtworkSource = streamApi.getAllStreams()
            .map { it.streams }
            .mapToDomain()
            .cacheToDB()

        return Single.concat(dbSource, newtworkSource)
    }

    override fun getSubStreams(): Flowable<List<Stream>> {
        val dbSource = streamDao.getSubscription()
            .mapToDomain()

        val newtworkSource = streamApi.getSubscriptionsStreams()
            .map { it.subscriptions }
            .mapToDomain()
            .cacheToDB(isSub = true)

        return Single.concat(dbSource, newtworkSource)
    }

    override fun getTopics(idStream: Long): Single<List<Topic>> {
        return streamApi.getTopics(idStream)
            .map { it.topics }
            .flatMapObservable(::fromIterable)
            .map { it.toTopicDomain() }
            .toList()
    }
}
