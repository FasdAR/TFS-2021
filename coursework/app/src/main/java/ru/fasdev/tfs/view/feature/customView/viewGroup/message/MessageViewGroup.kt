package ru.fasdev.tfs.view.feature.customView.viewGroup.message

import android.widget.TextView
import ru.fasdev.tfs.view.feature.customView.layout.FlexBoxLayout
import ru.fasdev.tfs.view.feature.customView.view.ReactionView
import ru.fasdev.tfs.view.ui.bottomDialog.emoji.adapter.viewTypes.EmojiUi

interface MessageViewGroup
{
    interface OnClickReactionListener {
        fun onClickReaction(reactionView: ReactionView, emoji: String)
    }

    interface OnClickPlusReactionListener {
        fun onClickPlusReaction()
    }

    val messageTextView: TextView
    val reactionsLayout: FlexBoxLayout
    var onClickReactionListener: OnClickReactionListener?
    var onClickPlusReactionListener: OnClickPlusReactionListener?
}