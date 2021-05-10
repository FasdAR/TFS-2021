package ru.fasdev.tfs.data.mapper

import ru.fasdev.tfs.data.source.network.events.model.Event
import ru.fasdev.tfs.data.source.network.messages.model.Message

fun Event.toMessage(): Message {
    return message ?: error("Null message")
}
