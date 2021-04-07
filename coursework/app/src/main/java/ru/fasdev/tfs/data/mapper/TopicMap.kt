package ru.fasdev.tfs.data.mapper

import ru.fasdev.tfs.domain.model.Topic
import ru.fasdev.tfs.screen.fragment.topicList.recycler.viewType.TopicUi

fun Topic.toTopicUi() = TopicUi(id, name, countMessage)
fun List<Topic>.mapToTopicUi() = map { it.toTopicUi() }
