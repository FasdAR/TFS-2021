package ru.fasdev.tfs.data.mapper

import ru.fasdev.tfs.domain.stream.model.Topic
import ru.fasdev.tfs.screen.fragment.streamList.recycler.viewType.TopicUi

fun Topic.toTopicUi(streamName: String) = TopicUi(this.lastMessageId, streamName, name, 0)
