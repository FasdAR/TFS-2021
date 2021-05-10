package ru.fasdev.tfs.data.newPck.source.network.messages.api

import io.reactivex.Single
import retrofit2.http.*
import ru.fasdev.tfs.data.newPck.source.network.messages.response.MessagesReactions
import ru.fasdev.tfs.data.newPck.source.network.messages.response.MessagesResponse
import ru.fasdev.tfs.data.newPck.source.network.messages.response.PostMessagesResponse

interface MessagesApi
{
    companion object {
        const val OLDEST_ANCHOR = "oldest"
        const val LIMIT_PAGE = 20
        const val TYPE_STREAM = "stream"
    }

    @GET("messages")
    fun getMessages(
        @Query("anchor") anchor: String = OLDEST_ANCHOR,
        @Query("num_before") numBefore: Int = 0,
        @Query("num_after") numAfter: Int = LIMIT_PAGE,
        @Query("narrow") narrow: String,
        @Query("client_gravatar") clientGravatar: Boolean = false,
        @Query("apply_markdown") applyMarkdown: Boolean = false,
        @Query("use_first_unread_anchor") useFirstUnreadAnchor: Boolean = false
    ) : Single<MessagesResponse>

    @POST("messages")
    fun sendMessage(
        @Query("type") type: String = TYPE_STREAM,
        @Query("to") to: String,
        @Query("content") content: String,
        @Query("subject") subject: String
    ): Single<PostMessagesResponse>

    @POST("messages/{message_id}/reactions")
    fun addReaction(@Path("message_id") messageId: Long, @Query("emoji_name") emojiName: String): Single<MessagesReactions>

    @DELETE("messages/{message_id}/reactions")
    fun deleteReaction(@Path("message_id") messageId: Long, @Query("emoji_name") emojiName: String): Single<MessagesReactions>
}