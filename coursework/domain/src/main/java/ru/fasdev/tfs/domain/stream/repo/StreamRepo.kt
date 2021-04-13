package ru.fasdev.tfs.domain.stream.repo

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import org.intellij.lang.annotations.Flow
import ru.fasdev.tfs.domain.stream.model.Stream
import ru.fasdev.tfs.domain.stream.model.Topic

interface StreamRepo {
    fun getAllStreams(): Flowable<List<Stream>>
    fun getSubStreams(): Flowable<List<Stream>>
    fun getTopics(idStream: Long): Flowable<List<Topic>>
    fun searchQuery(isSub: Boolean, query: String): Single<List<Stream>>
}
