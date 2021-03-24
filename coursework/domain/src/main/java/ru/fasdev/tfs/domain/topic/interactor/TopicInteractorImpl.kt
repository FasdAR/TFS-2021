package ru.fasdev.tfs.domain.topic.interactor

import ru.fasdev.tfs.domain.model.SubTopic
import ru.fasdev.tfs.domain.model.Topic
import ru.fasdev.tfs.domain.topic.repo.TopicRepo

class TopicInteractorImpl(private val topicRepo: TopicRepo): TopicInteractor
{
    override fun getAllTopics(): List<Topic> = topicRepo.getAllTopics()
    override fun getSubscribedTopics(): List<Topic> = topicRepo.getSubscribedTopics()
    override fun getTopicFromSubTopic(idSubTopic: Int): Topic? {
        return getAllTopics().find { it.subTopics.indexOfFirst { it.id == idSubTopic } != -1 }
    }
    override fun getAllSubTopicInTopic(idTopic: Int): List<SubTopic> {
        return getAllTopics().findLast { it.id == idTopic }?.subTopics ?: emptyList()
    }

    override fun searchByAllTopics(query: String): List<Topic> = topicRepo.searchByAllTopics(query)
    override fun searchBySubscribedTopics(query: String): List<Topic> = topicRepo.searchBySubscribedTopics(query)
}