package ru.fasdev.tfs.domain.stream.repo

import io.reactivex.rxjava3.core.Single
import ru.fasdev.tfs.domain.stream.model.Stream
import ru.fasdev.tfs.domain.stream.model.Topic

interface StreamRepo {
    fun getAllStreams(): Single<List<Stream>>
    fun getSubStreams(): Single<List<Stream>>
    fun getTopics(idStream: Long): Single<List<Topic>>
}
