package ru.fasdev.tfs.domain.old.stream.interactor

import io.reactivex.Flowable
import io.reactivex.Single
import ru.fasdev.tfs.domain.old.stream.model.Stream
import ru.fasdev.tfs.domain.old.stream.model.Topic

interface StreamInteractor {
    fun getAllStreams(): Flowable<List<Stream>>
    fun getSubStreams(): Flowable<List<Stream>>
    fun getAllTopics(idStream: Long): Flowable<List<Topic>>

    fun searchStream(query: String, isSub: Boolean): Single<List<Stream>>
}
