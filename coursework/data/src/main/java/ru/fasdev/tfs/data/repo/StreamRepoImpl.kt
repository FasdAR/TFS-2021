package ru.fasdev.tfs.data.repo

import io.reactivex.rxjava3.core.Observable.fromIterable
import io.reactivex.rxjava3.core.Single
import ru.fasdev.tfs.data.mapper.toStreamDomain
import ru.fasdev.tfs.data.mapper.toTopicDomain
import ru.fasdev.tfs.data.source.network.stream.api.StreamApi
import ru.fasdev.tfs.domain.stream.model.Stream
import ru.fasdev.tfs.domain.stream.model.Topic
import ru.fasdev.tfs.domain.stream.repo.StreamRepo

typealias NetworkStream = ru.fasdev.tfs.data.source.network.stream.model.Stream

class StreamRepoImpl(private val streamApi: StreamApi) : StreamRepo {
    private fun Single<List<NetworkStream>>.mapToDomain(): Single<List<Stream>> {
        return flatMapObservable(::fromIterable)
            .map { it.toStreamDomain() }
            .toList()
    }

    override fun getAllStreams(): Single<List<Stream>> {
        return streamApi.getAllStreams()
            .map { it.streams }
            .mapToDomain()
    }

    override fun getSubStreams(): Single<List<Stream>> {
        return streamApi.getSubscriptionsStreams()
            .map { it.subscriptions }
            .mapToDomain()
    }

    override fun getTopics(idStream: Long): Single<List<Topic>> {
        return streamApi.getTopics(idStream)
            .map { it.topics }
            .flatMapObservable(::fromIterable)
            .map { it.toTopicDomain() }
            .toList()
    }
}
