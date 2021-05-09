package ru.fasdev.tfs.data.old.mapper

import ru.fasdev.tfs.domain.old.stream.model.Stream
import ru.fasdev.tfs.recycler.item.stream.StreamItem

fun Stream.toStreamUi() = StreamItem(id.toInt(), name, false)
fun List<Stream>.mapToStreamUi() = map { it.toStreamUi() }
