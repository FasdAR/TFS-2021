package ru.fasdev.tfs.domain.stream.interactor

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.fasdev.tfs.domain.stream.model.Stream
import ru.fasdev.tfs.domain.stream.model.Topic
import ru.fasdev.tfs.domain.stream.repo.StreamRepo
import java.util.Locale

class StreamInteractorImpl(private val streamRepo: StreamRepo) : StreamInteractor {
    override fun getAllStreams(): Flowable<List<Stream>> {
        return streamRepo.getAllStreams()
    }

    override fun getSubStreams(): Flowable<List<Stream>> {
        return streamRepo.getSubStreams()
    }

    override fun getAllTopics(idStream: Long): Flowable<List<Topic>> {
        return streamRepo.getTopics(idStream)
    }

    override fun searchStream(query: String, isSub: Boolean): Single<List<Stream>> {
        val source = if (isSub) getSubStreams()
        else getAllStreams()

        return source
            .toList()
            .map { it[0] }
            .flatMapObservable { Observable.fromIterable(it) }
            .filter { it.name.trim().toLowerCase().contains(query.trim().toLowerCase()) }
            .toList()
    }
}
