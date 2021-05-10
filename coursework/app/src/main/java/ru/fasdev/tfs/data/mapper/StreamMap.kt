package ru.fasdev.tfs.data.mapper

import ru.fasdev.tfs.domain.stream.model.Stream
import ru.fasdev.tfs.recycler.item.stream.StreamItem

fun Stream.toStreamItem(): StreamItem {
    return StreamItem(
        uId = id.toInt(),
        nameTopic = name
    )
}