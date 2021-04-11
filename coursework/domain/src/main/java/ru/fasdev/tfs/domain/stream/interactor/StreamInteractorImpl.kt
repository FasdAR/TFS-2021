package ru.fasdev.tfs.domain.stream.interactor

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.fasdev.tfs.domain.stream.model.Stream
import ru.fasdev.tfs.domain.stream.model.Topic
import ru.fasdev.tfs.domain.stream.repo.StreamRepo
import java.util.Locale

class StreamInteractorImpl(private val streamRepo: StreamRepo) : StreamInteractor {
    override fun getAllStreams(): Single<List<Stream>> {
        return streamRepo.getAllStreams()
    }

    override fun getSubStreams(): Single<List<Stream>> {
        return streamRepo.getSubStreams()
    }

    override fun getAllTopics(idStream: Long): Single<List<Topic>> {
        return streamRepo.getTopics(idStream)
    }

    override fun searchStream(query: String, isSub: Boolean): Single<List<Stream>> {
        val source = if (isSub) getSubStreams()
        else getAllStreams()
        return source
            .flatMapObservable { items -> Observable.fromIterable(items) }
            .filter { it.name.toLowerCase(Locale.ROOT).contains(query.trim().toLowerCase(Locale.ROOT)) }
            .toList()
    }

    /*
    override fun getStream(id: Int): Single<Stream> {
        return getAllStreams()
            .flatMapObservable { items -> Observable.fromIterable(items) }
            .filter { it.id == id }
            .firstOrError()
            .subscribeOn(Schedulers.io())
    }

    override fun getTopic(id: Int): Single<Topic> {
        return getAllTopics()
            .flatMapObservable { items -> Observable.fromIterable(items) }
            .filter { it.id == id }
            .firstOrError()
            .subscribeOn(Schedulers.io())
    }

    override fun getStreamInTopic(idTopic: Int): Single<Stream> {
        return getTopic(idTopic)
            .flatMap { getStream(it.idStream) }
            .subscribeOn(Schedulers.io())
    }

    override fun getTopicsInStream(idStream: Int): Single<List<Topic>> {
        return getAllTopics()
            .flatMapObservable { items -> Observable.fromIterable(items) }
            .filter { it.idStream == idStream }
            .toList()
            .subscribeOn(Schedulers.io())
    }

    override fun searchStream(query: String): Single<List<Stream>> {
        return getAllStreams()
            .flatMapObservable { items -> Observable.fromIterable(items) }
            .filter { it.name.toLowerCase().contains(query.trim().toLowerCase()) }
            .toList()
            .subscribeOn(Schedulers.io())
    }
    */
}
