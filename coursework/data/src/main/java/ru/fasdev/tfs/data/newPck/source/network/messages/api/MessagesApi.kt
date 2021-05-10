package ru.fasdev.tfs.data.newPck.source.network.messages.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.fasdev.tfs.data.newPck.source.network.messages.response.MessagesResponse

interface MessagesApi
{
    companion object {
        const val OLDEST_ANCHOR = "oldest"
        const val LIMIT_PAGE = 20
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
}