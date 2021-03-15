package ru.fasdev.tfs.view.feature.customView.viewGroup.message

import android.widget.TextView
import ru.fasdev.tfs.view.feature.customView.layout.FlexBoxLayout
import ru.fasdev.tfs.view.feature.customView.view.ReactionView

interface MessageViewGroup
{
    interface OnClickReactionListener {
        fun onClick(reactionView: ReactionView)
    }

    val messageTextView: TextView
    val reactionsLayout: FlexBoxLayout
    var onClickReactionListener: OnClickReactionListener?
}