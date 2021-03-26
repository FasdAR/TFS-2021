package ru.fasdev.tfs.view.feature.mapper

import ru.fasdev.tfs.domain.model.Stream
import ru.fasdev.tfs.domain.model.Topic
import ru.fasdev.tfs.view.ui.fragment.topicList.adapter.viewType.TopicUi
import ru.fasdev.tfs.view.ui.fragment.topicList.adapter.viewType.StreamUi

fun Stream.toStreamUi() = StreamUi(id, name, false)
fun List<Stream>.mapToStreamUi() = map { it.toStreamUi() }

fun Topic.toTopicUi() = TopicUi(id, name, countMessage)
fun List<Topic>.mapToTopicUi() = map { it.toTopicUi() }
