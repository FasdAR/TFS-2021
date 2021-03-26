package ru.fasdev.tfs.view.ui.global.view.viewGroup.message

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.feature.util.getHeightMeasuredMargin
import ru.fasdev.tfs.view.feature.util.getWidthMeasuredMargin
import ru.fasdev.tfs.view.feature.util.layout
import ru.fasdev.tfs.view.ui.global.view.layout.FlexBoxLayout

class InternalMessageViewGroup
@JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : MessageViewGroupRoot(context, attributeSet, defStyleAttr, defStyleRes) {
    // #region View
    override val messageTextView: TextView
    override val reactionsLayout: FlexBoxLayout
    // #endregion

    // #region View Layout Params
    private val messageLayoutParams: MarginLayoutParams
        get() = messageTextView.layoutParams as MarginLayoutParams

    private val reactionsLayoutParams: MarginLayoutParams
        get() = reactionsLayout.layoutParams as MarginLayoutParams
    // #endregion

    // #region Rectangles Position View
    private val messageRect = Rect()
    private val reactionsRect = Rect()
    // #endregion

    init {
        LayoutInflater.from(context).inflate(R.layout.view_internal_message, this, true)

        messageTextView = findViewById(R.id.message_text)
        reactionsLayout = findViewById(R.id.reaction_layout)
        reactionsLayout.gravity = Gravity.RIGHT

        context.obtainStyledAttributes(attributeSet, R.styleable.InternalMessageViewGroup).apply {
            message = getString(R.styleable.InternalMessageViewGroup_mvMessage) ?: ""
            reactions = listOf()

            recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val maxWidth = MeasureSpec.getSize(widthMeasureSpec)

        // #region Measure Message Text Size
        val occupiedWidthSpace = maxWidth - MAX_MESSAGE_WIDTH
        measureChildWithMargins(
            messageTextView,
            widthMeasureSpec, occupiedWidthSpace,
            heightMeasureSpec, 0
        )

        val messageHeight = messageTextView.getHeightMeasuredMargin()
        val messageWidth = messageTextView.getWidthMeasuredMargin()
        // #endregion

        // #region Measure Reaction Layout Size
        measureChildWithMargins(
            reactionsLayout,
            widthMeasureSpec, SPACE_REACTIONS_EDGE,
            heightMeasureSpec, messageHeight
        )
        val reactionHeight = if (reactionsLayout.childCount == 0) 0
        else reactionsLayout.getHeightMeasuredMargin()
        // #endregion

        // #region Calculate Size
        val width = messageWidth
        val height = messageHeight + reactionHeight
        // #endregion

        val resolveWidth = resolveSize(width, widthMeasureSpec)
        val resolveHeight = resolveSize(height, heightMeasureSpec) + paddingTop + paddingBottom

        setMeasuredDimension(resolveWidth, resolveHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        messageRect.top = messageLayoutParams.topMargin + paddingTop
        messageRect.bottom = messageRect.top + messageTextView.measuredHeight
        messageRect.right = measuredWidth - paddingRight
        messageRect.left = messageRect.right - messageTextView.measuredWidth

        reactionsRect.top = messageRect.bottom + reactionsLayoutParams.topMargin
        reactionsRect.bottom = reactionsRect.top + reactionsLayout.measuredHeight
        reactionsRect.right = messageRect.right
        reactionsRect.left = reactionsRect.right - reactionsLayout.measuredWidth

        messageTextView.layout(messageRect)
        reactionsLayout.layout(reactionsRect)
    }
}
