package ru.fasdev.tfs.view.customView

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.util.toSp

class ReactionView
    @JvmOverloads
    constructor(
        context: Context,
        attributeSet: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = R.style.ReactionView
    ): View(context, attributeSet, defStyleAttr, defStyleRes)
{
    companion object {
        private const val DEFAULT_EMOJI = "\uD83D\uDE02"
        private val DEFAULT_TEXT_SIZE: Float = 14f.toSp
        private val DEFAULT_TEXT_COLOR = Color.parseColor("#CCCCCC")

        private val DRAWABLE_STATES = IntArray(1) {android.R.attr.state_selected}
    }

    //#region Pain Instrument
    private val textPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        textAlign = Paint.Align.CENTER
        color = DEFAULT_TEXT_COLOR
        textSize = DEFAULT_TEXT_SIZE
        isAntiAlias = true
    }
    //#endregion

    //#region Position Instrument
    private val textBounds: Rect = Rect()
    private val textPoint: PointF = PointF()
    //#endregion

    //#region Data Var
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

    private val text get() = "$emoji $reactionCount"
    //#endregion

    init {
        context.obtainStyledAttributes(attributeSet, R.styleable.ReactionView).apply {
            emoji = getString(R.styleable.ReactionView_rvEmoji) ?: DEFAULT_EMOJI
            reactionCount = getInt(R.styleable.ReactionView_rvCountReaction, 0)
            recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        textPaint.getTextBounds(text, 0, text.length, textBounds)

        val contentWidth = paddingLeft + paddingRight + textBounds.width()
        val contentHeight = paddingTop + paddingBottom + textBounds.height()

        textPoint.set(contentWidth.toFloat() / 2, contentHeight.toFloat() - (textBounds.height() / 2))

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

    fun selectedReaction() {
        isSelected = !isSelected
        if (isSelected) reactionCount += 1 else reactionCount -=1
    }
}