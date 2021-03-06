package ru.fasdev.tfs.view.customView

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
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
        const val DEFAULT_EMOJI = "\uD83D\uDE02"
        val DEFAULT_TEXT_SIZE: Float = 14f.toSp
        val DEFAULT_TEXT_COLOR = Color.parseColor("#CCCCCC")
    }

    //#region Pain Instrument
    private val textPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        textAlign = Paint.Align.CENTER
        color = DEFAULT_TEXT_COLOR
        textSize = DEFAULT_TEXT_SIZE
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

    val text
        get() = "$emoji $reactionCount"

    var isFixedReaction: Boolean = false
        set(value) {
            if (field != value) {
                field = value
                invalidate()
            }
        }
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

        setMeasuredDimension(contentWidth, contentHeight)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawText(text, textPoint.x, textPoint.y, textPaint)
    }
}