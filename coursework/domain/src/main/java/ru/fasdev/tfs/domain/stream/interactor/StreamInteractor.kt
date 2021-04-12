package ru.fasdev.tfs.domain.stream.interactor

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import ru.fasdev.tfs.domain.stream.model.Stream
import ru.fasdev.tfs.domain.stream.model.Topic

interface StreamInteractor {
    fun getAllStreams(): Flowable<List<Stream>>
    fun getSubStreams(): Flowable<List<Stream>>
    fun getAllTopics(idStream: Long): Single<List<Topic>>

    fun searchStream(query: String, isSub: Boolean): Single<List<Stream>>
}
