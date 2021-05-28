package ru.fasdev.tfs.data.mapper

import ru.fasdev.tfs.data.source.database.model.TopicDb
import ru.fasdev.tfs.data.source.network.streams.model.Topic

typealias TopicDomain = ru.fasdev.tfs.domain.stream.model.Topic

fun Topic.toTopicDomain(idStream: Long, index: Long): TopicDomain {
    return TopicDomain(idStream + index, name, 0) // TODO: CHANGE LAST MESSAGE COUNT
}

fun Topic.toTopicDb(idStream: Long, index: Long): TopicDb {
    return TopicDb(idStream + index, idStream, name)
}

fun TopicDb.toTopicDomain(): TopicDomain {
    return TopicDomain(id, name, 0)
}
