package ru.fasdev.tfs.view.feature.mapper

import ru.fasdev.tfs.domain.model.SubTopic
import ru.fasdev.tfs.domain.model.Topic
import ru.fasdev.tfs.view.ui.fragment.topicList.adapter.viewType.SubTopicUi
import ru.fasdev.tfs.view.ui.fragment.topicList.adapter.viewType.TopicUi

fun Topic.toTopicUi() = TopicUi(id, name, false)
fun List<Topic>.mapToTopicUi() = map { it.toTopicUi() }

fun SubTopic.toSubTopicUi() = SubTopicUi(id, name, countMessage)
fun List<SubTopic>.mapToSubTopicUi() = map { it.toSubTopicUi() }
