package ru.fasdev.tfs.domain.topic.interactor

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.fasdev.tfs.domain.model.Stream
import ru.fasdev.tfs.domain.model.Topic
import ru.fasdev.tfs.domain.topic.repo.TopicRepo

class TopicInteractorImpl(private val topicRepo: TopicRepo) : TopicInteractor {
    override fun getAllStreams(): Single<List<Stream>> {
        return Single.just(topicRepo.getAllStreams())
            .subscribeOn(Schedulers.io())
    }

    override fun getAllTopics(): Single<List<Topic>> {
        return Single.just(topicRepo.getAllTopics())
            .subscribeOn(Schedulers.io())
    }

    override fun getStream(id: Int): Single<Stream> {
        return getAllStreams().map { it.find { it.id == id }!! }
            .subscribeOn(Schedulers.io())
    }

    override fun getTopic(id: Int): Single<Topic> {
        return getAllTopics().map { it.find { it.id == id }!! }
            .subscribeOn(Schedulers.io())
    }

    override fun getStreamInTopic(idTopic: Int): Single<Stream> {
        return getTopic(idTopic).flatMap { getStream(it.idStream) }
            .subscribeOn(Schedulers.io())
    }

    override fun getTopicsInStream(idStream: Int): Single<List<Topic>> {
        return getAllTopics().map { it.filter { it.idStream == idStream } }
            .subscribeOn(Schedulers.io())
    }

    override fun searchStream(query: String): Single<List<Stream>> {
        return getAllStreams()
            .map { it.filter { it.name.toLowerCase().contains(query.trim().toLowerCase()) } }
            .subscribeOn(Schedulers.io())
    }
}
