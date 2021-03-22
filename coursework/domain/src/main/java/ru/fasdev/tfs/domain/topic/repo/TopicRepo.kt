package ru.fasdev.tfs.domain.topic.repo

import ru.fasdev.tfs.domain.model.Topic

interface TopicRepo
{
    fun getAllTopics(): List<Topic>
    fun getSubscribedTopics(): List<Topic>
    fun searchByAllTopics(query: String): List<Topic>
    fun searchBySubscribedTopics(query: String): List<Topic>
}