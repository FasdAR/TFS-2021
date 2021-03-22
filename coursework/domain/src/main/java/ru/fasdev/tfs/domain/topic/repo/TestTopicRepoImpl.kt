package ru.fasdev.tfs.domain.topic.repo

import ru.fasdev.tfs.domain.model.SubTopic
import ru.fasdev.tfs.domain.model.Topic

class TestTopicRepoImpl: TopicRepo
{
    override fun getAllTopics(): List<Topic> = listOf(
        Topic(
            id = 1,
            name = "general",
            subTopics = listOf(
                SubTopic(1, "Testing", 1240),
                SubTopic(2, "Bruh", 24)
            )
        ),
        Topic(
            id = 2,
            name = "Development",
            subTopics = listOf(
                SubTopic(3, "Android", 520),
                SubTopic(4, "Ios", 5699),
                SubTopic(5, "Windows Phone", 2)
            )
        ),
        Topic(
            id = 3,
            name = "Design",
            subTopics = listOf(
                SubTopic(6, "Mobile Android", 1),
                SubTopic(7, "Mobile Ios", 2),
                SubTopic(8, "Mobile WP", 3),
                SubTopic(9, "Web", 4),
                SubTopic(10, "PC", 5)
            )
        ),
        Topic(
            id = 4,
            name = "PR",
            subTopics = listOf(
                SubTopic(11, "Social", 156),
                SubTopic(12, "Blogs", 865),
                SubTopic(13, "Google", 286),
                SubTopic(14, "Yandex", 698)
            )
        )
    )

    override fun getSubscribedTopics(): List<Topic> = listOf(
        Topic(
            id = 2,
            name = "Development",
            subTopics = listOf(
                SubTopic(3, "Android", 520),
                SubTopic(4, "Ios", 5699),
                SubTopic(5, "Windows Phone", 2)
            )
        ),
        Topic(
            id = 3,
            name = "Design",
            subTopics = listOf(
                SubTopic(6, "Mobile Android", 1),
                SubTopic(7, "Mobile Ios", 2),
                SubTopic(8, "Mobile WP", 3),
                SubTopic(9, "Web", 4),
                SubTopic(10, "PC", 5)
            )
        )
    )

    override fun searchByAllTopics(query: String): List<Topic> = getAllTopics()
            .filter { it.name.toLowerCase().contains(query.trim().toLowerCase()) }

    override fun searchBySubscribedTopics(query: String): List<Topic> = getSubscribedTopics()
            .filter { it.name.toLowerCase().contains(query.trim().toLowerCase()) }
}