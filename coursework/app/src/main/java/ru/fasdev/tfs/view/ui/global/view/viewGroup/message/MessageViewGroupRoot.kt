package ru.fasdev.tfs.view.ui.global.view.viewGroup.message

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.feature.util.toDp
import ru.fasdev.tfs.view.ui.global.view.layout.FlexBoxLayout
import ru.fasdev.tfs.view.ui.global.view.view.ReactionView
import ru.fasdev.tfs.view.ui.global.view.viewGroup.message.model.MessageReactionUi

abstract class MessageViewGroupRoot
@JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attributeSet, defStyleAttr, defStyleRes), MessageViewGroup {
    companion object {
        val MAX_MESSAGE_WIDTH = 267.toDp
        val SPACE_REACTIONS_EDGE = 88.toDp
    }

    // #region View
    abstract override val messageTextView: TextView
    abstract override val reactionsLayout: FlexBoxLayout
    // #endregion

    // #region Listeners
    override var onClickReactionListener: MessageViewGroup.OnClickReactionListener? = null
    // #endregion

    // #region Data
    private var _reactions: MutableList<MessageReactionUi> = mutableListOf()

    var reactions: List<MessageReactionUi>
        set(value) {
            _reactions.clear()
            _reactions.addAll(value)
            updateReactions()
            requestLayout()
        }
        get() = _reactions

    var message: String = ""
        set(value) {
            if (field != value) {
                field = value

                updateMessage()
                requestLayout()
            }
        }
    // #endregion

    override fun generateDefaultLayoutParams() = MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    override fun generateLayoutParams(attrs: AttributeSet) = MarginLayoutParams(context, attrs)
    override fun generateLayoutParams(p: LayoutParams) = MarginLayoutParams(p)

    private fun selectedReaction(reactionView: ReactionView, emoji: String) {
        _reactions.find { it.emoji == emoji }?.let { reaction ->
            val index = _reactions.indexOf(reaction)
            val newSelected = !reaction.isSelected
            val newCount = if (newSelected) reaction.reactionCount + 1 else reaction.reactionCount - 1
            if (newCount == 0) {
                _reactions.removeAt(index)
                updateReactions()
            } else {
                val newReaction = reaction.copy(isSelected = newSelected, reactionCount = newCount)
                _reactions.removeAt(index)
                _reactions.add(index, newReaction)
                reactionView.selectedReaction()
            }
        }
    }

    private fun updateReactions() {
        reactionsLayout.removeAllViews()

        reactions.forEach { reaction ->
            val reactionView = ReactionView(context)

            reactionView.reactionCount = reaction.reactionCount
            reactionView.emoji = reaction.emoji
            reactionView.isSelectedReaction = reaction.isSelected

            reactionView.setOnClickListener {
                val reactionFindView = it as ReactionView
                selectedReaction(reactionFindView, reaction.emoji)
                onClickReactionListener?.onClickReaction(reactionFindView, reaction.emoji)
            }

            reactionsLayout.addView(reactionView)
        }

        if (reactions.isNotEmpty()) {
            val addImageView = ImageView(context)
            addImageView.scaleType = ImageView.ScaleType.CENTER
            addImageView.layoutParams = LayoutParams(ReactionView.MIN_WIDTH, ReactionView.MIN_HEIGHT)
            addImageView.setBackgroundResource(R.drawable.sh_reaction)
            addImageView.setImageResource(R.drawable.ic_plus)

            addImageView.setOnClickListener {
                onClickReactionListener?.onClickAddNewReaction()
            }
            reactionsLayout.addView(addImageView)
        }
    }

    private fun updateMessage() {
        messageTextView.text = message
    }
}
