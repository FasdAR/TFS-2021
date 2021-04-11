package ru.fasdev.tfs.view.message.base.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MessageReactionUi(val emoji: String, val emojiName: String, val reactionCount: Int, var isSelected: Boolean = false) : Parcelable
