package ru.fasdev.tfs.data.newPck.mapper

import ru.fasdev.tfs.data.newPck.source.network.streams.model.Topic

typealias TopicDomain = ru.fasdev.tfs.domain.newPck.stream.model.Topic
fun Topic.toTopicDomain(idStream: Long, index: Long): TopicDomain {
    return TopicDomain(idStream + index, name)
}