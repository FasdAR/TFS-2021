package ru.fasdev.tfs.view.ui.global.view.viewGroup.message

import android.widget.TextView
import ru.fasdev.tfs.view.ui.global.view.layout.FlexBoxLayout
import ru.fasdev.tfs.view.ui.global.view.view.ReactionView

interface MessageViewGroup {
    interface OnClickReactionListener {
        fun onClickReaction(reactionView: ReactionView, emoji: String)
        fun onClickAddNewReaction()
    }

    val messageTextView: TextView
    val reactionsLayout: FlexBoxLayout
    var onClickReactionListener: OnClickReactionListener?
}
