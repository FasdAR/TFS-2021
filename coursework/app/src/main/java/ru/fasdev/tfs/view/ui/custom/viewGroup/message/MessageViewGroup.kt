package ru.fasdev.tfs.view.ui.custom.viewGroup.message

import android.widget.TextView
import ru.fasdev.tfs.view.ui.custom.layout.FlexBoxLayout
import ru.fasdev.tfs.view.ui.custom.view.ReactionView

interface MessageViewGroup
{
    interface OnClickReactionListener {
        fun onClick(reactionView: ReactionView)
    }

    val messageTextView: TextView
    val reactionsLayout: FlexBoxLayout
    var onClickReactionListener: OnClickReactionListener?
}