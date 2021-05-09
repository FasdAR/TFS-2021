package ru.fasdev.tfs.data.newPck.repository.streams

import io.reactivex.Observable
import io.reactivex.Single
import ru.fasdev.tfs.data.newPck.mapper.toStreamDomain
import ru.fasdev.tfs.data.newPck.mapper.toTopicDomain
import ru.fasdev.tfs.data.newPck.source.network.streams.api.StreamsApi
import ru.fasdev.tfs.data.newPck.source.network.users.api.UsersApi
import ru.fasdev.tfs.domain.newPck.stream.model.Stream
import ru.fasdev.tfs.domain.newPck.stream.model.Topic

class StreamsRepositoryImpl(private val userApi: UsersApi, private val streamApi: StreamsApi) : StreamsRepository
{
    override fun getAllStreams(): Observable<List<Stream>> {
        return streamApi.getAllStreams()
            .flatMap {
                Observable.fromIterable(it.streams)
                    .map { it.toStreamDomain() }
                    .toList()
            }
            .toObservable()
    }

    override fun getOwnSubsStreams(): Observable<List<Stream>> {
        return userApi.getOwnSubscriptions()
            .flatMap {
                Observable.fromIterable(it.subscriptions)
                    .map { it.toStreamDomain() }
                    .toList()
            }
            .toObservable()
    }

    override fun getOwnTopics(idStream: Long): Observable<List<Topic>> {
        return userApi.getOwnTopics(idStream)
            .map {
                it.topics.withIndex()
                    .map { it.value.toTopicDomain(idStream, it.index.toLong()) }
            }
            .toObservable()
    }

    override fun searchQuery(query: String, isAmongSubs: Boolean): Single<List<Stream>> {
        //TODO: SEARCH IN DATA BASE
        return Single.just(listOf())
    }
}