package ru.fasdev.tfs.view.feature.mapper

import ru.fasdev.tfs.domain.model.Stream
import ru.fasdev.tfs.view.ui.fragment.topicList.adapter.viewType.SubTopicUi
import ru.fasdev.tfs.view.ui.fragment.topicList.adapter.viewType.TopicUi

fun Stream.toTopicUi() = TopicUi(id, name, false)
fun List<Stream>.mapToTopicUi() = map { it.toTopicUi() }

fun Stream.toSubTopicUi() = SubTopicUi(id, name, countMessage)
fun List<Stream>.mapToSubTopicUi() = map { it.toSubTopicUi() }
