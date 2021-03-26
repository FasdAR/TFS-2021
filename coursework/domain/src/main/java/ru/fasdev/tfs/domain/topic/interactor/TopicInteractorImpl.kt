package ru.fasdev.tfs.domain.topic.interactor

import ru.fasdev.tfs.domain.model.SubTopic
import ru.fasdev.tfs.domain.model.Topic
import ru.fasdev.tfs.domain.topic.repo.TopicRepo

class TopicInteractorImpl(private val topicRepo: TopicRepo) : TopicInteractor {
    override fun getAllTopics(): List<Topic> = topicRepo.getAllTopics()

    override fun getAllSubTopics(): List<SubTopic> = topicRepo.getAllSubTopics()

    override fun getMainTopic(idTopic: Int): Topic? = getAllTopics().find { it.id == idTopic }
    override fun getSubTopic(subTopicId: Int): SubTopic? = getAllSubTopics().find { it.id == subTopicId }

    override fun getMainTopicInSubTopic(subTopicId: Int): Topic? {
        val idTopic = getAllSubTopics().find { it.id == subTopicId }?.rootIdTopic
        return getAllTopics().find { it.id == idTopic }
    }

    override fun getSubTopicsInMainTopic(rootId: Int) = getAllSubTopics().filter { it.rootIdTopic == rootId }

    override fun searchTopics(query: String): List<Topic> {
        return if (query.isNotEmpty())
            getAllTopics().filter { it.name.toLowerCase().contains(query.trim().toLowerCase()) }
        else
            getAllTopics()
    }
}
