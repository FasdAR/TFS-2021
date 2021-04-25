package ru.fasdev.tfs.view.ui.global.view.viewGroup.message

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.isDigitsOnly
import ru.fasdev.tfs.R
import ru.fasdev.tfs.databinding.ViewExternalMessageBinding
import ru.fasdev.tfs.view.feature.util.getHeightMeasuredMargin
import ru.fasdev.tfs.view.feature.util.getWidthMeasuredMargin
import ru.fasdev.tfs.view.feature.util.layout
import ru.fasdev.tfs.view.ui.global.view.layout.FlexBoxLayout

class ExternalMessageViewGroup
@JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : MessageViewGroupRoot(context, attributeSet, defStyleAttr, defStyleRes) {
    // #region View
    private val avatarImageView: ImageView
    private val nameTextView: TextView
    private val messageLayout: ViewGroup
    override val messageTextView: TextView
    override val reactionsLayout: FlexBoxLayout
    // #endregion

    // #region View Layout Params
    private val avatarLayoutParams: MarginLayoutParams
        get() = avatarImageView.layoutParams as MarginLayoutParams

    private val messageLayoutParams: MarginLayoutParams
        get() = messageLayout.layoutParams as MarginLayoutParams

    private val reactionsLayoutParams: MarginLayoutParams
        get() = reactionsLayout.layoutParams as MarginLayoutParams
    // #endregion

    // #region Rectangles Position View
    private val avatarRect = Rect()
    private val messageRect = Rect()
    private val reactionsRect = Rect()
    // #endregion

    // #region Data
    var avatarSrc: String = R.drawable.ic_launcher_background.toString()
        set(value) {
            if (field != value) {
                field = value
            }

            updateAvatar()
        }

    var name: String = ""
        set(value) {
            if (field != value) {
                field = value
                updateName()
                requestLayout()
            }
        }
    // #endregion

    init {
        ViewExternalMessageBinding.inflate(LayoutInflater.from(context), this)

        // #region FindView
        avatarImageView = findViewById(R.id.avatar)
        nameTextView = findViewById(R.id.name_text)
        messageLayout = findViewById(R.id.msg_layout)
        messageTextView = findViewById(R.id.message_text)
        reactionsLayout = findViewById(R.id.reaction_layout)
        // #endregion

        context.obtainStyledAttributes(attributeSet, R.styleable.ExternalMessageViewGroup).apply {
            avatarSrc = getResourceId(
                R.styleable.ExternalMessageViewGroup_mvSrcAvatar,
                R.drawable.ic_launcher_background
            ).toString()
            message = getString(R.styleable.ExternalMessageViewGroup_mvMessage) ?: ""
            name = getString(R.styleable.ExternalMessageViewGroup_mvName) ?: ""

            recycle()
        }

        reactions = listOf()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val maxWidth = MeasureSpec.getSize(widthMeasureSpec)

        // #region Measure Avatar Size
        measureChildWithMargins(
            avatarImageView,
            widthMeasureSpec, 0,
            heightMeasureSpec, 0
        )
        val avatarHeight = avatarImageView.getHeightMeasuredMargin()
        val avatarWidth = avatarImageView.getWidthMeasuredMargin()
        // #endregion

        // #region Measure Message Layout Size
        val occupiedWidthSpace = avatarWidth + (maxWidth - MAX_MESSAGE_WIDTH)
        measureChildWithMargins(
            messageLayout,
            widthMeasureSpec, occupiedWidthSpace,
            heightMeasureSpec, 0
        )
        val messageHeight = messageLayout.getHeightMeasuredMargin()
        val messageWidth = messageLayout.getWidthMeasuredMargin()
        // #endregion

        // #region Measure Reaction Layout Size
        measureChildWithMargins(
            reactionsLayout,
            widthMeasureSpec, avatarWidth + SPACE_REACTIONS_EDGE,
            heightMeasureSpec, messageHeight
        )

        val reactionHeight = if (reactionsLayout.childCount == 0) 0
        else reactionsLayout.getHeightMeasuredMargin()

        val reactionWidth = reactionsLayout.getWidthMeasuredMargin()
        // #endregion

        // #region Calculate Size
        var width = avatarWidth + messageWidth
        val resultReactionWidth = reactionWidth - messageWidth
        if (resultReactionWidth > 0) width += resultReactionWidth

        val height = maxOf(avatarHeight, messageHeight) + reactionHeight
        // #endregion

        val resolveWidth = resolveSize(width, widthMeasureSpec) + paddingLeft + paddingRight
        val resolveHeight = resolveSize(height, heightMeasureSpec) + paddingTop + paddingBottom

        setMeasuredDimension(resolveWidth, resolveHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        // #region Calculate Avatar Position
        avatarRect.left = avatarLayoutParams.leftMargin + paddingLeft
        avatarRect.top = avatarLayoutParams.topMargin + paddingTop
        avatarRect.right = avatarRect.left + avatarImageView.measuredWidth
        avatarRect.bottom = avatarRect.top + avatarImageView.measuredHeight
        // #endregion

        // #region Calculate Message Position
        messageRect.left = avatarImageView.getWidthMeasuredMargin() + messageLayoutParams.leftMargin + paddingLeft
        messageRect.top = messageLayoutParams.topMargin + paddingTop
        messageRect.right = messageRect.left + messageLayout.measuredWidth
        messageRect.bottom = messageRect.top + messageLayout.measuredHeight
        // #endregion

        // #region Calculate Reactions Position
        reactionsRect.left = messageRect.left
        reactionsRect.top = messageRect.bottom + reactionsLayoutParams.topMargin
        reactionsRect.right = reactionsRect.left + reactionsLayout.measuredWidth
        reactionsRect.bottom = reactionsRect.top + reactionsLayout.measuredHeight
        // #endregion

        avatarImageView.layout(avatarRect)
        messageLayout.layout(messageRect)
        reactionsLayout.layout(reactionsRect)
    }

    private fun updateAvatar() {
        if (avatarSrc.isNotEmpty()) {
            if (avatarSrc.isDigitsOnly()) {
                avatarImageView.setImageResource(avatarSrc.toInt())
            } else {
                // TODO: LOAD FROM OTHER METHOD IMAGE
            }
        }
    }

    private fun updateName() {
        nameTextView.text = name
    }
}
