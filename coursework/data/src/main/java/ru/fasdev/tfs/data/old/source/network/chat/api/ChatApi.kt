package ru.fasdev.tfs.data.old.source.network.chat.api

import io.reactivex.Single
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.fasdev.tfs.data.old.source.network.chat.response.AllMessagesResponse
import ru.fasdev.tfs.data.old.source.network.chat.response.EmojiResponse
import ru.fasdev.tfs.data.old.source.network.chat.response.SendMessageResponse

interface ChatApi {
    @GET("messages")
    fun getAllMessages(
        @Query("anchor") anchor: String = "oldest",
        @Query("num_before") numBefore: Int = 0,
        @Query("num_after") numAfter: Int = 1000,
        @Query("narrow") narrow: String,
        @Query("client_gravatar") clientGravatar: Boolean = false,
        @Query("apply_markdown") applyMarkdown: Boolean = false,
        @Query("use_first_unread_anchor") useFirstUnreadAnchor: Boolean = false
    ): Single<AllMessagesResponse>

    @POST("messages")
    fun sendMessage(
        @Query("type") type: String = "stream",
        @Query("to") to: String,
        @Query("content") content: String,
        @Query("subject") subject: String
    ): Single<SendMessageResponse>

    @POST("messages/{message_id}/reactions")
    fun addReaction(@Path("message_id") messageId: Int, @Query("emoji_name") emojiName: String): Single<EmojiResponse>

    @DELETE("messages/{message_id}/reactions")
    fun removeReaction(@Path("message_id") messageId: Int, @Query("emoji_name") emojiName: String): Single<EmojiResponse>
}
