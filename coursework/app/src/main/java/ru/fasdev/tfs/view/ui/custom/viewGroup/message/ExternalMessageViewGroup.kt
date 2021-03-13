package ru.fasdev.tfs.view.ui.custom.viewGroup.message

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
import ru.fasdev.tfs.view.feature.util.getHeightMeasuredMargin
import ru.fasdev.tfs.view.feature.util.getWidthMeasuredMargin
import ru.fasdev.tfs.view.feature.util.layout
import ru.fasdev.tfs.view.ui.custom.layout.FlexboxLayout

class ExternalMessageViewGroup
    @JvmOverloads constructor(
            context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0,
            defStyleRes: Int = 0
    ) : SimpleMessageViewGroup(context, attrs, defStyleAttr, defStyleRes)
{
    private val avatarImageView: ImageView
    private val nameTextView: TextView
    override val msgTextView: TextView
    private val msgLayout: ViewGroup
    override val reactionLayout: FlexboxLayout

    // #region Layout Params
    private val avatarLayoutParams: MarginLayoutParams
        get() = avatarImageView.layoutParams as MarginLayoutParams

    private val msgLayoutParams: MarginLayoutParams
        get() = msgLayout.layoutParams as MarginLayoutParams

    private val reactionLayoutParams: MarginLayoutParams
        get() = reactionLayout.layoutParams as MarginLayoutParams
    // #endregion

    // #region Rect
    private val avatarRect = Rect()
    private val msgRect = Rect()
    private val reactionRect = Rect()
    // #endregion

    //#region Data
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
    //#endregion
    init {
        LayoutInflater.from(context).inflate(R.layout.view_external_message, this, true)

        avatarImageView = findViewById(R.id.avatar)
        msgLayout = findViewById(R.id.msg_layout)
        reactionLayout = findViewById(R.id.reaction_layout)

        nameTextView = findViewById(R.id.name_text)
        msgTextView = findViewById(R.id.message_text)

        context.obtainStyledAttributes(attrs, R.styleable.ExternalMessageViewGroup).apply {
            avatarSrc = getResourceId(
                    R.styleable.ExternalMessageViewGroup_srcAvatar,
                    R.drawable.ic_launcher_background
            ).toString()
            msgText = getString(R.styleable.ExternalMessageViewGroup_msgText) ?: ""
            name = getString(R.styleable.ExternalMessageViewGroup_nameText) ?: ""
            msgViewMaxSize = getMsgViewMaxSize()

            recycle()
        }

        updateReactionLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val maxWidth = MeasureSpec.getSize(widthMeasureSpec)

        // #region Avatar Size
        measureChildWithMargins(
                avatarImageView, widthMeasureSpec, 0,
                heightMeasureSpec, 0
        )

        val avatarWidth = avatarImageView.getWidthMeasuredMargin()
        // #endregion

        // #region MsgLayout Size
        var occupiedSpace = avatarWidth + (maxWidth - msgViewMaxSize)

        if (msgViewMaxSize == MATCH_PARENT) {
            occupiedSpace = avatarWidth
        }

        measureChildWithMargins(
                msgLayout, widthMeasureSpec, occupiedSpace,
                heightMeasureSpec, 0
        )

        val msgHeight = msgLayout.getHeightMeasuredMargin()
        val msgWidth = msgLayout.getWidthMeasuredMargin()
        // #endregion

        // #region ReactionLayout Size
        measureChildWithMargins(
                reactionLayout, widthMeasureSpec, avatarWidth,
                heightMeasureSpec, msgHeight
        )

        val reactionHeight = if (reactionLayout.childCount == 0) 0
        else reactionLayout.getHeightMeasuredMargin()

        val reactionWidth = reactionLayout.getWidthMeasuredMargin()
        // #endregion

        // #region Width Calculate
        var width = avatarWidth + msgWidth

        val resultReactionWidth = reactionWidth - msgWidth
        if (resultReactionWidth > 0) width += resultReactionWidth
        // #endregion

        // #region Height Calculate
        val height = msgHeight + reactionHeight
        // #endregion

        val resolveWidth = resolveSize(width, widthMeasureSpec)
        val resolveHeight = resolveSize(height, heightMeasureSpec)

        setMeasuredDimension(resolveWidth, resolveHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int)
    {
        // #region Avatar Calculate Size
        avatarRect.left = avatarLayoutParams.leftMargin
        avatarRect.top = avatarLayoutParams.topMargin
        avatarRect.right = avatarRect.left + avatarImageView.measuredWidth
        avatarRect.bottom = avatarRect.top + avatarImageView.measuredHeight
        // #endregion

        // #region Msg Calculate Size
        msgRect.left = avatarImageView.getWidthMeasuredMargin() + msgLayoutParams.leftMargin
        msgRect.top = msgLayoutParams.topMargin
        msgRect.right = msgRect.left + msgLayout.measuredWidth
        msgRect.bottom = msgRect.top + msgLayout.measuredHeight
        // #endregion

        // #region Reaction Layout Calculate Size
        reactionRect.left = msgRect.left
        reactionRect.top = msgRect.bottom + reactionLayoutParams.topMargin
        reactionRect.right = reactionRect.left + reactionLayout.measuredWidth
        reactionRect.bottom = reactionRect.top + reactionLayout.measuredHeight
        // #endregion

        avatarImageView.layout(avatarRect)
        msgLayout.layout(msgRect)
        reactionLayout.layout(reactionRect)
    }

    override fun generateDefaultLayoutParams() = MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT)
    override fun generateLayoutParams(attrs: AttributeSet) = MarginLayoutParams(context, attrs)
    override fun generateLayoutParams(p: LayoutParams) = MarginLayoutParams(p)

    private fun updateAvatar() {
        if (avatarSrc.isDigitsOnly()) {
            avatarImageView.setImageResource(avatarSrc.toInt())
        }
    }

    private fun updateNameText() {
        nameTextView.text = name
    }
}