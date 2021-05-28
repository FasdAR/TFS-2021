package ru.fasdev.tfs.data.mapper

import ru.fasdev.tfs.domain.stream.model.Topic
import ru.fasdev.tfs.recycler.item.topic.TopicItem

fun Topic.toTopicItem(idStream: Long): TopicItem {
    return TopicItem(
        uId = id.toInt(),
        idStream = idStream,
        nameTopic = name,
        messageCount = lastMessageCount
    )
}
