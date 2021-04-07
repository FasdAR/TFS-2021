package ru.fasdev.tfs.view.message

import android.widget.TextView
import ru.fasdev.tfs.view.flexBox.FlexBoxLayout
import ru.fasdev.tfs.view.reaction.ReactionView

interface MessageViewGroup {
    interface OnClickReactionListener {
        fun onClickReaction(reactionView: ReactionView, emoji: String)
        fun onClickAddNewReaction()
    }

    val messageTextView: TextView
    val reactionsLayout: FlexBoxLayout
    var onClickReactionListener: OnClickReactionListener?
}
