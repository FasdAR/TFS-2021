package ru.fasdev.tfs.domain.topic.interactor

import ru.fasdev.tfs.domain.model.Stream
import ru.fasdev.tfs.domain.model.Topic

interface TopicInteractor {
    fun getAllStreams(): List<Stream>
    fun getAllTopics(): List<Topic>

    fun getStream(id: Int): Stream?
    fun getTopic(id: Int): Topic?

    fun getStreamInTopic(idTopic: Int): Stream?
    fun getTopicsInStream(idStream: Int): List<Topic>
    fun searchStream(query: String): List<Stream>
}
