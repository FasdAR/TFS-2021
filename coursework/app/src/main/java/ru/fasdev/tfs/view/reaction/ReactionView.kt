package ru.fasdev.tfs.view.reaction

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import ru.fasdev.tfs.R
import ru.fasdev.tfs.core.ext.toDp
import ru.fasdev.tfs.core.ext.toSp

class ReactionView
@JvmOverloads
constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = R.style.Widget_TFS_ReactionView
) : View(context, attributeSet, defStyleAttr, defStyleRes) {
    companion object {
        internal val MIN_HEIGHT = 30.toDp
        internal val MIN_WIDTH = 45.toDp

        private const val DEFAULT_EMOJI = "\uD83D\uDE02"
        private val DEFAULT_TEXT_SIZE: Float = 14f.toSp
        private val DEFAULT_TEXT_COLOR = Color.parseColor("#CCCCCC")

        private val DRAWABLE_STATES = IntArray(1) { android.R.attr.state_selected }
    }

    // #region Pain Instrument
    private val textPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        textAlign = Paint.Align.CENTER
        color = selectedTextColor
        textSize = DEFAULT_TEXT_SIZE
        isAntiAlias = true
    }
    // #endregion

    // #region Position Instrument
    private val textBounds: Rect = Rect()
    private val textPoint: PointF = PointF()
    // #endregion

    // #region Data Var
    var emoji: String = DEFAULT_EMOJI
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    var reactionCount: Int = 0
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    var isSelectedReaction: Boolean = false
        set(value) {
            if (field != value) {
                field = value

                isSelected = isSelectedReaction

                updateSelectedState()
            }
        }

    var selectedTextColor: Int = DEFAULT_TEXT_COLOR
        set(value) {
            field = value
            updateSelectedState()
        }
    var unSelectedTextColor: Int = DEFAULT_TEXT_COLOR
        set(value) {
            field = value
            updateSelectedState()
        }

    private val text get() = "$emoji $reactionCount"
    // #endregion

    init {
        context.obtainStyledAttributes(attributeSet, R.styleable.ReactionView).apply {
            emoji = getString(R.styleable.ReactionView_rvEmoji) ?: DEFAULT_EMOJI
            reactionCount = getInt(R.styleable.ReactionView_rvCountReaction, 0)
            selectedTextColor = getColor(
                R.styleable.ReactionView_rvSelectedTextColor,
                ContextCompat.getColor(context, R.color.white_200)
            )
            unSelectedTextColor = getColor(
                R.styleable.ReactionView_rvUnselectedTextColor,
                ContextCompat.getColor(context, R.color.grey_400)
            )

            recycle()
        }

        updateSelectedState()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        textPaint.getTextBounds(text, 0, text.length, textBounds)

        var contentWidth = paddingLeft + paddingRight + textBounds.width()
        var contentHeight = textBounds.height()

        if (contentHeight < MIN_HEIGHT) contentHeight = MIN_HEIGHT
        if (contentWidth < MIN_WIDTH) contentWidth = MIN_WIDTH

        textPoint.set(contentWidth.toFloat() / 2, contentHeight.toFloat() / 2 + (textBounds.height() / 4))

        setMeasuredDimension(resolveSize(contentWidth, widthMeasureSpec), resolveSize(contentHeight, heightMeasureSpec))
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawText(text, textPoint.x, textPoint.y, textPaint)
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + DRAWABLE_STATES.size)

        if (isSelected) {
            mergeDrawableStates(drawableState, DRAWABLE_STATES)
        }

        return drawableState
    }

    private fun updateSelectedState() {
        if (isSelected) textPaint.color = selectedTextColor
        else textPaint.color = unSelectedTextColor

        invalidate()
    }

    fun selectedReaction() {
        isSelectedReaction = !isSelectedReaction
        if (isSelected) reactionCount += 1 else reactionCount -= 1
    }
}
