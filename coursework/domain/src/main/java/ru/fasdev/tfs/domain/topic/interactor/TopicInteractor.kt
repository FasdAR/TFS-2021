package ru.fasdev.tfs.domain.topic.interactor

import ru.fasdev.tfs.domain.model.SubTopic
import ru.fasdev.tfs.domain.model.Topic

interface TopicInteractor
{
    fun getAllTopics(): List<Topic>
    fun getAllSubTopics(): List<SubTopic>

    fun getMainTopic(idTopic: Int): Topic?
    fun getSubTopic(subTopicId: Int): SubTopic?

    fun getMainTopicInSubTopic(subTopicId: Int): Topic?
    fun getSubTopicsInMainTopic(rootId: Int): List<SubTopic>
    fun searchTopics(query: String): List<Topic>
}