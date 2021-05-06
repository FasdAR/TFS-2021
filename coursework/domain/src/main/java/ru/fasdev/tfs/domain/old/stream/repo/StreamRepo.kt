package ru.fasdev.tfs.domain.old.stream.repo

import io.reactivex.Flowable
import io.reactivex.Single
import ru.fasdev.tfs.domain.old.stream.model.Stream
import ru.fasdev.tfs.domain.old.stream.model.Topic

interface StreamRepo {
    fun getAllStreams(): Flowable<List<Stream>>
    fun getSubStreams(): Flowable<List<Stream>>
    fun getTopics(idStream: Long): Flowable<List<Topic>>
    fun searchQuery(isSub: Boolean, query: String): Single<List<Stream>>
}
