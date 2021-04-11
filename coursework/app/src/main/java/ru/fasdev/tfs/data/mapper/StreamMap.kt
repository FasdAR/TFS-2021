package ru.fasdev.tfs.data.mapper

import ru.fasdev.tfs.domain.stream.model.Stream
import ru.fasdev.tfs.screen.fragment.streamList.recycler.viewType.StreamUi

fun Stream.toStreamUi() = StreamUi(id.toInt(), name, false)
fun List<Stream>.mapToStreamUi() = map { it.toStreamUi() }
