package ru.fasdev.tfs.domain.old.message.repo

import io.reactivex.Completable
import io.reactivex.Flowable
import ru.fasdev.tfs.domain.old.message.model.Message

interface MessageRepo {
    fun getMessagesByTopic(nameStream: String, nameTopic: String, anchorMessage: Long, limit: Int, afterCount: Int, beforeCount: Int, isFirstLoad: Boolean): Flowable<List<Message>>
    fun sendMessage(nameStream: String, nameTopic: String, message: String): Completable
    fun addEmoji(messageId: Int, emojiName: String): Completable
    fun removeEmoji(messageId: Int, emojiName: String): Completable
}
