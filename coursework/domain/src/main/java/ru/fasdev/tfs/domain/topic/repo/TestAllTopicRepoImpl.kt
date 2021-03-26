package ru.fasdev.tfs.domain.topic.repo

import ru.fasdev.tfs.domain.model.SubTopic
import ru.fasdev.tfs.domain.model.Topic

class TestAllTopicRepoImpl : TopicRepo {
    override fun getAllTopics(): List<Topic> = listOf(
        Topic(
            id = 1,
            name = "general"
        ),
        Topic(
            id = 2,
            name = "Development"
        ),
        Topic(
            id = 3,
            name = "Design"
        ),
        Topic(
            id = 4,
            name = "PR"
        )
    )

    override fun getAllSubTopics(): List<SubTopic> = listOf(
        SubTopic(1, 1, "Testing", 1240),
        SubTopic(2, 1, "Bruh", 24),
        SubTopic(3, 2, "Android", 520),
        SubTopic(4, 2, "Ios", 5699),
        SubTopic(5, 2, "Windows Phone", 2),
        SubTopic(6, 3, "Mobile Android", 1),
        SubTopic(7, 3, "Mobile Ios", 2),
        SubTopic(8, 3, "Mobile WP", 3),
        SubTopic(9, 3, "Web", 4),
        SubTopic(10, 3, "PC", 5),
        SubTopic(11, 4, "Social", 156),
        SubTopic(12, 4, "Blogs", 865),
        SubTopic(13, 4, "Google", 286),
        SubTopic(14, 4, "Yandex", 698)
    )
}
