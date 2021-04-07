package ru.fasdev.tfs.data.mapper

import ru.fasdev.tfs.domain.model.Stream
import ru.fasdev.tfs.screen.fragment.topicList.recycler.viewType.StreamUi

fun Stream.toStreamUi() = StreamUi(id, name, false)
fun List<Stream>.mapToStreamUi() = map { it.toStreamUi() }