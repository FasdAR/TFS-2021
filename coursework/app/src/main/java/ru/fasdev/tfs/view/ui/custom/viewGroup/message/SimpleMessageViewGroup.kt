package ru.fasdev.tfs.view.ui.custom.viewGroup.message

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.feature.util.toDp
import ru.fasdev.tfs.view.ui.custom.layout.FlexboxLayout
import ru.fasdev.tfs.view.ui.custom.view.ReactionView
import ru.fasdev.tfs.view.ui.custom.viewGroup.message.model.MessageReactionUi

abstract class SimpleMessageViewGroup
    @JvmOverloads constructor(
            context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0,
            defStyleRes: Int = 0
    ) : ViewGroup(context, attrs, defStyleAttr, defStyleRes)
{
    interface OnClickReactionListener {
        fun onClick(reactionView: ReactionView)
    }

    companion object {
        internal val MAX_MSG_VIEW_SIZE = 260.toDp
    }

    val onClickReactionListener: OnClickReactionListener? = null

    abstract val msgTextView: TextView
    abstract val reactionLayout: FlexboxLayout

    //#region Data Region
    var reactionList: ArrayList<MessageReactionUi> = arrayListOf()
        set(value) {
            if (field != value) {
                field.clear()
                field.addAll(value)

                updateReactionLayout()
                requestLayout()
            }
        }

    var msgText: String = ""
        set(value) {
            if (field != value) {
                field = value

                updateMsgText()
                requestLayout()
            }
        }

    var msgViewMaxSize: Int = MAX_MSG_VIEW_SIZE
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }
    //#ednregion

    internal fun updateMsgText() {
        msgTextView.text = msgText
    }

    internal fun updateReactionLayout() {
        reactionLayout.removeAllViews()

        reactionList.forEach { reaction ->
            val reactionView = ReactionView(context)

            reactionView.reactionCount = reaction.reactionCount
            reactionView.emoji = reaction.emoji
            reactionView.isSelectedReaction = reaction.isSelected

            reactionView.setOnClickListener {
                onClickReactionListener?.onClick(it as ReactionView)
            }

            reactionLayout.addView(reactionView)
        }

        val addImageView = ImageView(context)
        addImageView.scaleType = ImageView.ScaleType.CENTER
        addImageView.layoutParams = LayoutParams(ReactionView.MIN_WIDTH, ReactionView.MIN_HEIGHT)
        addImageView.setBackgroundResource(R.drawable.sh_reaction)
        addImageView.setImageResource(R.drawable.ic_plus)

        reactionLayout.addView(addImageView)
    }

    internal fun TypedArray.getMsgViewMaxSize(): Int {
        val type = getType(R.styleable.ExternalMessageViewGroup_msgViewMaxSize)

        return when {
            type == TypedValue.TYPE_DIMENSION -> {
                getDimensionPixelSize(
                        R.styleable.ExternalMessageViewGroup_msgViewMaxSize,
                        MAX_MSG_VIEW_SIZE
                )
            }
            type >= TypedValue.TYPE_FIRST_INT && type <= TypedValue.TYPE_LAST_INT -> {
                getInt(R.styleable.ExternalMessageViewGroup_msgViewMaxSize, LayoutParams.MATCH_PARENT)
            }
            else -> error("Don't support type msgViewMaxSize")
        }
    }
}