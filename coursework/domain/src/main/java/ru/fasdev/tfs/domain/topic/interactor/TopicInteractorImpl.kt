package ru.fasdev.tfs.domain.topic.interactor

import ru.fasdev.tfs.domain.model.Topic
import ru.fasdev.tfs.domain.topic.repo.TopicRepo

class TopicInteractorImpl(private val topicRepo: TopicRepo): TopicInteractor
{
    override fun getAllTopics(): List<Topic> = topicRepo.getAllTopics()
    override fun getSubscribedTopics(): List<Topic> = topicRepo.getSubscribedTopics()
    override fun searchByAllTopics(query: String): List<Topic> = topicRepo.searchByAllTopics(query)
    override fun searchBySubscribedTopics(query: String): List<Topic> = topicRepo.searchBySubscribedTopics(query)
}