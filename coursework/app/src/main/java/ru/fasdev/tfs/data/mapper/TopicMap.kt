package ru.fasdev.tfs.data.mapper

import ru.fasdev.tfs.domain.stream.model.Topic
import ru.fasdev.tfs.screen.fragment.topicList.recycler.viewType.TopicUi

fun Topic.toTopicUi() = TopicUi(this.lastMessageId, name, 0)
fun List<Topic>.mapToTopicUi() = map { it.toTopicUi() }
