package ru.fasdev.tfs.domain.topic.repo

import ru.fasdev.tfs.domain.model.SubTopic
import ru.fasdev.tfs.domain.model.Topic

interface TopicRepo
{
    fun getAllTopics(): List<Topic>
    fun getAllSubTopics(): List<SubTopic>
}