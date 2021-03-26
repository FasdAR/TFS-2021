package ru.fasdev.tfs.domain.topic.repo

import ru.fasdev.tfs.domain.model.Stream
import ru.fasdev.tfs.domain.model.Topic

class TestSubscribedTopicRepoImpl : TopicRepo {
    override fun getAllStreams(): List<Stream> = listOf(
        Stream(
            id = 2,
            name = "Development"
        ),
        Stream(
            id = 3,
            name = "Design"
        )
    )

    override fun getAllTopics(): List<Topic> = listOf(
        Topic(3, 2, "Android", 520),
        Topic(4, 2, "Ios", 5699),
        Topic(5, 2, "Windows Phone", 2),
        Topic(6, 3, "Mobile Android", 1),
        Topic(7, 3, "Mobile Ios", 2),
        Topic(8, 3, "Mobile WP", 3),
        Topic(9, 3, "Web", 4),
        Topic(10, 3, "PC", 5)
    )
}
