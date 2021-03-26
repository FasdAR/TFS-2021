package ru.fasdev.tfs.domain.topic.repo

import ru.fasdev.tfs.domain.model.Stream
import ru.fasdev.tfs.domain.model.Topic

interface TopicRepo {
    fun getAllStreams(): List<Stream>
    fun getAllTopics(): List<Topic>
}
