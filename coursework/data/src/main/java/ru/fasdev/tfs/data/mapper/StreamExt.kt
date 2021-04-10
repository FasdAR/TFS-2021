package ru.fasdev.tfs.data.mapper

import ru.fasdev.tfs.data.source.network.stream.model.Stream
import ru.fasdev.tfs.data.source.network.stream.model.Topic

typealias DomainStream = ru.fasdev.tfs.domain.stream.model.Stream
typealias TopicStream = ru.fasdev.tfs.domain.stream.model.Topic

fun Stream.toStreamDomain() = DomainStream(this.streamId, this.name)
fun Topic.toTopicDomain() = TopicStream(this.name, this.maxId)