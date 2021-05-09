package ru.fasdev.tfs.data.newPck.mapper

import ru.fasdev.tfs.data.newPck.source.database.model.StreamDb
import ru.fasdev.tfs.data.newPck.source.network.streams.model.Stream

typealias StreamDomain = ru.fasdev.tfs.domain.newPck.stream.model.Stream
fun Stream.toStreamDomain(): StreamDomain {
    return StreamDomain(streamId, name)
}

fun StreamDb.toStreamDomain(): StreamDomain {
    return StreamDomain(id, name)
}

fun Stream.toStreamDb(isAmongSubs: Boolean): StreamDb {
    return StreamDb(streamId, name, isAmongSubs)
}