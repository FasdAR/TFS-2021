package ru.fasdev.tfs.data.old.mapper

import ru.fasdev.tfs.domain.old.stream.model.Stream
import ru.fasdev.tfs.screen.fragment.streamList.recycler.viewType.StreamUi

fun Stream.toStreamUi() = StreamUi(id.toInt(), name, false)
fun List<Stream>.mapToStreamUi() = map { it.toStreamUi() }
