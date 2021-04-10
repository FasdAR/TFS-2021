package ru.fasdev.tfs.domain.stream.interactor

import io.reactivex.rxjava3.core.Single
import ru.fasdev.tfs.domain.stream.model.Stream
import ru.fasdev.tfs.domain.stream.model.Topic

interface StreamInteractor {
    fun getAllStreams(): Single<List<Stream>>
    fun getSubStreams(): Single<List<Stream>>
    fun getAllTopics(idStream: Long): Single<List<Topic>>
}
