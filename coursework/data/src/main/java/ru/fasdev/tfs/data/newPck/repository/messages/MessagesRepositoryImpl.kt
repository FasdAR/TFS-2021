package ru.fasdev.tfs.data.newPck.repository.messages

import io.reactivex.Completable
import io.reactivex.Observable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.fasdev.tfs.data.newPck.mapper.toMessageDomain
import ru.fasdev.tfs.data.newPck.source.network.base.model.ZulipResult
import ru.fasdev.tfs.data.newPck.source.network.messages.api.MessagesApi
import ru.fasdev.tfs.data.newPck.source.network.base.model.Narrow
import ru.fasdev.tfs.data.newPck.source.network.events.manager.EventsManager
import ru.fasdev.tfs.data.newPck.source.network.events.model.EventType
import ru.fasdev.tfs.domain.newPck.message.model.Message

class MessagesRepositoryImpl(
    private val json: Json,
    private val messagesApi: MessagesApi,
    private val eventsManager: EventsManager
) : MessagesRepository {
    companion object {
        private const val USER_ID = 402233L
        private const val ANCHOR_NEWEST = "newest"

        private const val OPERATOR_STREAM = "stream"
        private const val OPERATOR_TOPIC = "topic"
    }

    private var currentListMessages: List<Message> = listOf()

    override fun getMessagesPage(
        nameStream: String,
        nameTopic: String,
        idAnchorMessage: Long?,
        afterMessageCount: Int,
        beforeMessageCount: Int
    ): Observable<List<Message>> {
        val narrowStream = Narrow(operator = OPERATOR_STREAM, operand = nameStream)
        val narrowTopic = Narrow(operator = OPERATOR_TOPIC, operand = nameTopic)
        val narrowJson = json.encodeToString(listOf(narrowStream, narrowTopic))

        val anchorMessage: String = idAnchorMessage?.toString() ?: ANCHOR_NEWEST

        return messagesApi.getMessages(
            anchor = anchorMessage,
            numBefore = beforeMessageCount,
            numAfter = afterMessageCount,
            narrow = narrowJson
        )
            .map { it.messages }
            .flatMapObservable {
                Observable.fromIterable(it)
                    .map { it.toMessageDomain(USER_ID) }
                    .toSortedList { item1, item2 -> item1.date.compareTo(item2.date) }
                    .toObservable()
            }
            .doOnNext {
                //ТЕСТОВОЕ КЭШИРОВАНИЕ
                currentListMessages = (currentListMessages + it).distinct()
            }
    }

    override fun sendMessage(nameStream: String, nameTopic: String, message: String): Completable {
        return messagesApi.sendMessage(
            to = nameStream,
            subject = nameTopic,
            content = message
        ).flatMapCompletable {
            Completable.fromCallable { it.result == ZulipResult.SUCCESS }
        }
    }

    override fun addReaction(messageId: Long, emojiName: String): Completable {
        return messagesApi.addReaction(messageId, emojiName)
            .flatMapCompletable { Completable.fromCallable { it.result == ZulipResult.SUCCESS } }
    }

    override fun removeReaction(messageId: Long, emojiName: String): Completable {
        return messagesApi.deleteReaction(messageId, emojiName)
            .flatMapCompletable { Completable.fromCallable { it.result == ZulipResult.SUCCESS } }
    }

    override fun listenUpdate(nameStream: String, nameTopic: String): Observable<List<Message>> {
        val narrowStream = Narrow(operator = OPERATOR_STREAM, operand = nameStream)
        val narrowTopic = Narrow(operator = OPERATOR_TOPIC, operand = nameTopic)
        val narrows = listOf(narrowStream, narrowTopic)

        val eventTypes = listOf(EventType.MESSAGE, EventType.REACTION)

        return eventsManager.startListen(narrows, eventTypes)
            .flatMap {
                Observable.fromIterable(it)
                    .map { event ->
                        when (event.type) {
                            EventType.REACTION -> {
                                return@map currentListMessages.find {it.id == event.messageId} ?: error("Nothing Reaction")
                            }
                            EventType.MESSAGE -> {
                                return@map event.message?.toMessageDomain(USER_ID) ?: error("Error map message")
                            }
                        }
                    }
                    .toSortedList { item1, item2 -> item1.date.compareTo(item2.date) }
                    .toObservable()
            }
    }
}