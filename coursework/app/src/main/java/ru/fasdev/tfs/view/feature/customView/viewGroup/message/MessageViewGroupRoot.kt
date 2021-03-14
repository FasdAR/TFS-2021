package ru.fasdev.tfs.view.feature.customView.viewGroup.message

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.feature.util.toDp
import ru.fasdev.tfs.view.feature.customView.layout.FlexBoxLayout
import ru.fasdev.tfs.view.feature.customView.view.ReactionView
import ru.fasdev.tfs.view.feature.customView.viewGroup.message.model.MessageReactionUi

abstract class MessageViewGroupRoot
    @JvmOverloads constructor(
            context: Context,
            attributeSet: AttributeSet? = null,
            defStyleAttr: Int = 0,
            defStyleRes: Int = 0
    ) : ViewGroup(context, attributeSet, defStyleAttr, defStyleRes), MessageViewGroup
{
    companion object {
        val MAX_MESSAGE_WIDTH = 267.toDp
    }

    //#region View
    abstract override val messageTextView: TextView
    abstract override val reactionsLayout: FlexBoxLayout
    //#endregion

    //#region Listeners
    override var onClickReactionListener: MessageViewGroup.OnClickReactionListener? = null
    //#endregion

    //#region Data
    var reactions: List<MessageReactionUi> = listOf()
        set(value) {
            if (field != value) {
                field = value
                updateReactions()
                requestLayout()
            }
        }

    var message: String = ""
        set(value) {
            if (field != value) {
                field = value

                updateMessage()
                requestLayout()
            }
        }
    //#endregion

    override fun generateDefaultLayoutParams() = MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    override fun generateLayoutParams(attrs: AttributeSet) = MarginLayoutParams(context, attrs)
    override fun generateLayoutParams(p: LayoutParams) = MarginLayoutParams(p)

    private fun updateReactions() {
        reactionsLayout.removeAllViews()

        reactions.forEach { reaction ->
            val reactionView = ReactionView(context)

            reactionView.reactionCount = reaction.reactionCount
            reactionView.emoji = reaction.emoji
            reactionView.isSelectedReaction = reaction.isSelected

            reactionView.setOnClickListener {
                onClickReactionListener?.onClick(it as ReactionView)
            }

            reactionsLayout.addView(reactionView)
        }

        if (reactions.isNotEmpty()) {
            val addImageView = ImageView(context)
            addImageView.scaleType = ImageView.ScaleType.CENTER
            addImageView.layoutParams = LayoutParams(ReactionView.MIN_WIDTH, ReactionView.MIN_HEIGHT)
            addImageView.setBackgroundResource(R.drawable.sh_reaction)
            addImageView.setImageResource(R.drawable.ic_plus)

            reactionsLayout.addView(addImageView)
        }
    }

    private fun updateMessage() {
        messageTextView.text = message
    }
}