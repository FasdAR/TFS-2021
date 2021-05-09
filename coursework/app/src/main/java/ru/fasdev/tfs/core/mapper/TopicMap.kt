package ru.fasdev.tfs.data.old.mapper

import ru.fasdev.tfs.domain.old.stream.model.Topic
import ru.fasdev.tfs.recycler.item.topic.TopicItem

fun Topic.toTopicUi(streamName: String) = TopicItem(this.id.toInt(), streamName, name, 0)
