package ru.fasdev.tfs.data.old.mapper

import ru.fasdev.tfs.domain.old.stream.model.Topic
import ru.fasdev.tfs.screen.fragment.streamList.recycler.viewType.TopicUi

fun Topic.toTopicUi(streamName: String) = TopicUi(this.id.toInt(), streamName, name, 0)
