package ru.fasdev.tfs.domain.stream.interactor

import io.reactivex.Flowable
import io.reactivex.Single
import ru.fasdev.tfs.domain.stream.model.Stream
import ru.fasdev.tfs.domain.stream.model.Topic
import ru.fasdev.tfs.domain.stream.repo.StreamRepo

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
        return streamRepo.searchQuery(isSub, query)
    }
}
