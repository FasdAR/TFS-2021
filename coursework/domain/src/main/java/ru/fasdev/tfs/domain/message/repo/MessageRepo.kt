package ru.fasdev.tfs.domain.message.repo

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import ru.fasdev.tfs.domain.message.model.Message

interface MessageRepo {
    fun getMessagesByTopic(nameStream: String, nameTopic: String, anchorMessage: Long, limit: Int, afterCount: Int, beforeCount: Int, isFirstLoad: Boolean): Flowable<List<Message>>
    fun sendMessage(nameStream: String, nameTopic: String, message: String): Completable
    fun addEmoji(messageId: Int, emojiName: String): Completable
    fun removeEmoji(messageId: Int, emojiName: String): Completable
}
