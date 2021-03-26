package ru.fasdev.tfs.domain.topic.repo

import ru.fasdev.tfs.domain.model.SubTopic
import ru.fasdev.tfs.domain.model.Topic

class TestSubscribedTopicRepoImpl : TopicRepo {
    override fun getAllTopics(): List<Topic> = listOf(
        Topic(
            id = 2,
            name = "Development"
        ),
        Topic(
            id = 3,
            name = "Design"
        )
    )

    override fun getAllSubTopics(): List<SubTopic> = listOf(
        SubTopic(3, 2, "Android", 520),
        SubTopic(4, 2, "Ios", 5699),
        SubTopic(5, 2, "Windows Phone", 2),
        SubTopic(6, 3, "Mobile Android", 1),
        SubTopic(7, 3, "Mobile Ios", 2),
        SubTopic(8, 3, "Mobile WP", 3),
        SubTopic(9, 3, "Web", 4),
        SubTopic(10, 3, "PC", 5)
    )
}
