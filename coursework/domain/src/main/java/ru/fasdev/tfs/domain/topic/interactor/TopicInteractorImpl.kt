package ru.fasdev.tfs.domain.topic.interactor

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.fasdev.tfs.domain.model.Stream
import ru.fasdev.tfs.domain.model.Topic
import ru.fasdev.tfs.domain.testEnv
import ru.fasdev.tfs.domain.topic.repo.TopicRepo

class TopicInteractorImpl(private val topicRepo: TopicRepo) : TopicInteractor {
    override fun getAllStreams(): Single<List<Stream>> {
        return Single.just(topicRepo.getAllStreams())
            .testEnv("Get all streams")
            .subscribeOn(Schedulers.io())
    }

    override fun getAllTopics(): Single<List<Topic>> {
        return Single.just(topicRepo.getAllTopics())
            .testEnv("Get all topics")
            .subscribeOn(Schedulers.io())
    }

    override fun getStream(id: Int): Single<Stream> {
        return getAllStreams()
            .testEnv("Get stream")
            .flatMapObservable { items -> Observable.fromIterable(items) }
            .filter { it.id == id }
            .firstOrError()
            .subscribeOn(Schedulers.io())
    }

    override fun getTopic(id: Int): Single<Topic> {
        return getAllTopics()
            .testEnv("Get topic")
            .flatMapObservable { items -> Observable.fromIterable(items) }
            .filter { it.id == id }
            .firstOrError()
            .subscribeOn(Schedulers.io())
    }

    override fun getStreamInTopic(idTopic: Int): Single<Stream> {
        return getTopic(idTopic)
            .testEnv("Get stream in topic")
            .flatMap { getStream(it.idStream) }
            .subscribeOn(Schedulers.io())
    }

    override fun getTopicsInStream(idStream: Int): Single<List<Topic>> {
        return getAllTopics()
            .testEnv("Get all topics")
            .flatMapObservable { items -> Observable.fromIterable(items) }
            .filter { it.idStream == idStream }
            .toList()
            .subscribeOn(Schedulers.io())
    }

    override fun searchStream(query: String): Single<List<Stream>> {
        return getAllStreams()
            .testEnv("Search stream")
            .flatMapObservable { items -> Observable.fromIterable(items) }
            .filter { it.name.toLowerCase().contains(query.trim().toLowerCase()) }
            .toList()
            .subscribeOn(Schedulers.io())
    }
}
