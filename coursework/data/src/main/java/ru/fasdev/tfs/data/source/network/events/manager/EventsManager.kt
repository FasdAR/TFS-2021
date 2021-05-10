package ru.fasdev.tfs.data.source.network.events.manager

import io.reactivex.Observable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.fasdev.tfs.data.source.network.base.model.Narrow
import ru.fasdev.tfs.data.source.network.base.model.ZulipResult
import ru.fasdev.tfs.data.source.network.events.api.EventsApi
import ru.fasdev.tfs.data.source.network.events.model.Event
import ru.fasdev.tfs.data.source.network.events.model.EventType

class EventsManager(private val json: Json, private val eventsApi: EventsApi) {
    private companion object {
        private const val BAD_EVENT_QUEUE_ID = "BAD_EVENT_QUEUE_ID"
        private const val BAD_REQUEST = "BAD_REQUEST"
    }

    private var lastId = -1
    private val lastEventId get() = lastId

    fun startListen(narrows: List<Narrow>, eventTypes: List<EventType>): Observable<List<Event>> {
        val narrowJson = "${narrows.map { "[\"${it.operator}\",\"${it.operand.replace("\"", "\\\"")}\"]" }}"
        val eventTypesJson = json.encodeToString(eventTypes)

        return eventsApi.registerQueue(eventTypesJson, narrowJson)
            .doOnSuccess { lastId = -1 }
            .flatMapObservable { registerResp ->
                Observable.defer {
                    eventsApi.events(registerResp.queueId, lastEventId)
                        .map { event ->
                            if (event.result == ZulipResult.ERROR && event.code == BAD_EVENT_QUEUE_ID) {
                                throw BadQueueIdError(registerResp.queueId)
                            } else {
                                lastId = event.events.last().id
                                return@map event.events
                            }
                        }
                        .toObservable()
                }
                    .repeat()
            }
            .retry()
    }

    class BadQueueIdError(idQueue: String) : Throwable()
}
