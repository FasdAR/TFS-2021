package ru.fasdev.tfs.view.customView

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.isDigitsOnly
import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.model.ReactionUiModel
import ru.fasdev.tfs.view.util.getHeightMeasuredMargin
import ru.fasdev.tfs.view.util.getWidthMeasuredMargin
import ru.fasdev.tfs.view.util.layout
import ru.fasdev.tfs.view.util.toDp

class MessageView
    @JvmOverloads
    constructor(
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
        private val MAX_MSG_VIEW_SIZE = 260.toDp
    }

    private val avatarImageView: ImageView
    private val msgLayout: ViewGroup
    private val reactionLayout: FlexBoxLayout

    private val msgTextView: TextView
    private val nameTextView: TextView

    //#region Layout Params
    private val avatarLayoutParams: MarginLayoutParams
        get() = avatarImageView.layoutParams as MarginLayoutParams

    private val msgLayoutParams: MarginLayoutParams
        get() = msgLayout.layoutParams as MarginLayoutParams

    private val reactionLayoutParams: MarginLayoutParams
        get() = reactionLayout.layoutParams as MarginLayoutParams
    //#endregion

    //#region Rect
    private val avatarRect = Rect()
    private val msgRect = Rect()
    private val reactionRect = Rect()
    //#endregion

    //#region Data
    var onClickReactionListener: OnClickReactionListener? = null

    var reactionList: ArrayList<ReactionUiModel> = arrayListOf()
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

    var name: String = ""
        set(value) {
            if (field != value) {
                field = value

                updateNameText()
                requestLayout()
            }
        }

    var avatarSrc: String = R.drawable.ic_launcher_background.toString()
        set(value) {
            if (field != value) {
                field = value
                updateAvatar()
            }
        }

    var msgViewMaxSize: Int = MAX_MSG_VIEW_SIZE
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }
    //#endregion

    init {
        LayoutInflater.from(context).inflate(R.layout.message_view, this, true)

        avatarImageView = findViewById(R.id.avatar)
        msgLayout = findViewById(R.id.msg_layout)
        reactionLayout = findViewById(R.id.reaction_layout)

        nameTextView = findViewById(R.id.name_text)
        msgTextView = findViewById(R.id.message_text)

        updateAvatar()
        updateNameText()
        updateMsgText()
        updateReactionLayout()

        context.obtainStyledAttributes(attrs, R.styleable.MessageView).apply {
            avatarSrc = getResourceId(R.styleable.MessageView_srcAvatar,
                    R.drawable.ic_launcher_background).toString()
            msgText = getString(R.styleable.MessageView_msgText) ?: ""
            name = getString(R.styleable.MessageView_nameText) ?: ""

            val type = getType(R.styleable.MessageView_msgViewMaxSize)

            when
            {
                type == TypedValue.TYPE_DIMENSION -> {
                    msgViewMaxSize = getDimensionPixelSize(R.styleable.MessageView_msgViewMaxSize,
                            MAX_MSG_VIEW_SIZE)
                }
                type >= TypedValue.TYPE_FIRST_INT && type <= TypedValue.TYPE_LAST_INT -> {
                    msgViewMaxSize = getInt(R.styleable.MessageView_msgViewMaxSize, MATCH_PARENT)
                }
            }

            recycle()
        }
    }

    private fun updateAvatar() {
        if (avatarSrc.isDigitsOnly()) {
            avatarImageView.setImageResource(avatarSrc.toInt())
        }
    }

    private fun updateNameText() {
        nameTextView.text = name
    }

    private fun updateMsgText() {
        msgTextView.text = msgText
    }

    private fun updateReactionLayout() {
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

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val maxWidth = MeasureSpec.getSize(widthMeasureSpec)

        //#region Avatar Size
        measureChildWithMargins(avatarImageView, widthMeasureSpec, 0,
                heightMeasureSpec, 0)

        val avatarWidth = avatarImageView.getWidthMeasuredMargin()
        //#endregion

        //#region MsgLayout Size
        var occupiedSpace = avatarWidth + (maxWidth - msgViewMaxSize)

        if (msgViewMaxSize == MATCH_PARENT) {
            occupiedSpace = avatarWidth
        }

        measureChildWithMargins(msgLayout, widthMeasureSpec, occupiedSpace,
                heightMeasureSpec, 0)

        val msgHeight = msgLayout.getHeightMeasuredMargin()
        val msgWidth = msgLayout.getWidthMeasuredMargin()
        //#endregion

        //#region ReactionLayout Size
        measureChildWithMargins(reactionLayout, widthMeasureSpec, avatarWidth,
                heightMeasureSpec, msgHeight)

        val reactionHeight = if (reactionLayout.childCount == 0) 0
            else reactionLayout.getHeightMeasuredMargin()

        val reactionWidth = reactionLayout.getWidthMeasuredMargin()
        //#endregion

        //#region Width Calculate
        var width = avatarWidth + msgWidth

        val resultReactionWidth = reactionWidth - msgWidth
        if (resultReactionWidth > 0) width += resultReactionWidth
        //#endregion

        //#region Height Calculate
        val height = msgHeight + reactionHeight
        //#endregion

        val resolveWidth = resolveSize(width, widthMeasureSpec)
        val resolveHeight = resolveSize(height, heightMeasureSpec)

        setMeasuredDimension(resolveWidth, resolveHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        //#region Avatar Calculate Size
        avatarRect.left = avatarLayoutParams.leftMargin
        avatarRect.top = avatarLayoutParams.topMargin
        avatarRect.right = avatarRect.left + avatarImageView.measuredWidth
        avatarRect.bottom = avatarRect.top + avatarImageView.measuredHeight
        //#endregion

        //#region Msg Calculate Size
        msgRect.left = avatarImageView.getWidthMeasuredMargin() + msgLayoutParams.leftMargin
        msgRect.top = msgLayoutParams.topMargin
        msgRect.right = msgRect.left + msgLayout.measuredWidth
        msgRect.bottom = msgRect.top + msgLayout.measuredHeight
        //#endregion

        //#region Reaction Layout Calculate Size
        reactionRect.left = msgRect.left
        reactionRect.top = msgRect.bottom + reactionLayoutParams.topMargin
        reactionRect.right = reactionRect.left + reactionLayout.measuredWidth
        reactionRect.bottom = reactionRect.top + reactionLayout.measuredHeight
        //#endregion

        avatarImageView.layout(avatarRect)
        msgLayout.layout(msgRect)
        reactionLayout.layout(reactionRect)
    }

    override fun generateDefaultLayoutParams() = MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT)
    override fun generateLayoutParams(attrs: AttributeSet) = MarginLayoutParams(context, attrs)
    override fun generateLayoutParams(p: LayoutParams) = MarginLayoutParams(p)
}