package ru.fasdev.tfs.data.mapper

import ru.fasdev.tfs.data.source.database.model.StreamDb
import ru.fasdev.tfs.data.source.network.streams.model.Stream

typealias StreamDomain = ru.fasdev.tfs.domain.stream.model.Stream
fun Stream.toStreamDomain(): StreamDomain {
    return StreamDomain(streamId, name)
}

fun StreamDb.toStreamDomain(): StreamDomain {
    return StreamDomain(id, name)
}

fun StreamDomain.toStreamDb(isAmongSubs: Boolean): StreamDb {
    return StreamDb(id, name, isAmongSubs)
}
