package ru.fasdev.tfs.data.old.mapper

import ru.fasdev.tfs.data.old.source.db.model.StreamDB
import ru.fasdev.tfs.data.old.source.network.stream.model.Stream
import ru.fasdev.tfs.data.old.source.network.stream.model.Topic

typealias DomainStream = ru.fasdev.tfs.domain.old.stream.model.Stream
typealias TopicStream = ru.fasdev.tfs.domain.old.stream.model.Topic

fun Stream.toStreamDomain() = DomainStream(this.streamId, this.name)
fun Topic.toTopicDomain(idStream: Long) = TopicStream(this.name.toConstHash().toLong() + idStream, this.name)

fun String.toConstHash(): Int {
    val alphabet = "abcdefghijklmnopqrstuvwxyz"
    var result = 0
    forEach {
        val index = alphabet.indexOf(it)
        if (index != -1) {
            val number = alphabet.indexOf(it)
            result += number
        }
        else if (it.isDigit()) {
            result += it.toInt()
        }
    }

    return result
}

fun StreamDB.toStreamDomain() = DomainStream(this.id, this.name)
