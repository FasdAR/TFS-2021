package ru.fasdev.tfs.view.ui.global.view.viewGroup.message.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MessageReactionUi(val emoji: String, val reactionCount: Int, var isSelected: Boolean = false) : Parcelable
