package ru.fasdev.tfs.domain.topic.interactor

import ru.fasdev.tfs.domain.model.Stream
import ru.fasdev.tfs.domain.model.Topic
import ru.fasdev.tfs.domain.topic.repo.TopicRepo

class TopicInteractorImpl(private val topicRepo: TopicRepo) : TopicInteractor {
    override fun getAllStreams(): List<Stream> = topicRepo.getAllStreams()
    override fun getAllTopics(): List<Topic> = topicRepo.getAllTopics()

    override fun getStream(id: Int): Stream? = getAllStreams().find { it.id == id }
    override fun getTopic(id: Int): Topic? = getAllTopics().find { it.id == id }

    override fun getStreamInTopic(idTopic: Int): Stream? {
        val idStream = getTopic(idTopic)?.idStream ?: -1
        return getStream(idStream)
    }

    override fun getTopicsInStream(idStream: Int): List<Topic> {
        return getAllTopics().filter { it.idStream == idStream }
    }

    override fun searchStream(query: String): List<Stream> {
        return getAllStreams().apply {
            if (query.isNotEmpty()) {
                filter { it.name.toLowerCase().contains(query.trim().toLowerCase()) }
            }
        }
    }
}
