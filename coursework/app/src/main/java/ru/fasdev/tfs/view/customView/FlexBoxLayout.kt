package ru.fasdev.tfs.view.customView

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.children
import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.util.layout
import ru.fasdev.tfs.view.util.toDp

class FlexBoxLayout
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
    ): ViewGroup(context, attrs, defStyleAttr, defStyleRes)
{
    companion object {
        private val DEFAULT_VERTICAL_SPACE = 7.toDp
        private val DEFAULT_HORIZONTAL_SPACE = 10.toDp
    }

    var verticalSpace = DEFAULT_VERTICAL_SPACE
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    var horizontalSpace = DEFAULT_HORIZONTAL_SPACE
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    init {
        setWillNotDraw(false)

        context.obtainStyledAttributes(attrs, R.styleable.FlexBoxLayout).apply {
            verticalSpace = getDimension(R.styleable.FlexBoxLayout_android_verticalSpacing,
                    DEFAULT_VERTICAL_SPACE.toFloat()).toInt()
            horizontalSpace = getDimension(R.styleable.FlexBoxLayout_android_horizontalSpacing,
                    DEFAULT_HORIZONTAL_SPACE.toFloat()).toInt()
            recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val maxWidth = MeasureSpec.getSize(widthMeasureSpec)

        var cursorY = 0
        var cursorX = 0

        var maxHeightRow = 0
        var maxWidthRow = 0

        children.forEach { child ->
            measureChild(child, widthMeasureSpec,  heightMeasureSpec)

            //#region Checking New Line
            val nexWidth = cursorX + child.measuredWidth
            if (nexWidth > maxWidth) {
                cursorX = 0
                cursorY += maxHeightRow + verticalSpace
                maxHeightRow = 0
            }
            //#endregion

            //#region Calculate Next Position
            cursorX += child.measuredWidth + horizontalSpace

            if (nexWidth > maxWidthRow) maxWidthRow = nexWidth
            if (child.measuredHeight > maxHeightRow) maxHeightRow = child.measuredHeight
            //#endregion
        }

        val calculateWidth = maxWidthRow
        val calculateHeight = cursorY + maxHeightRow

        val resolveWidth = resolveSize(calculateWidth, widthMeasureSpec)
        val resolveHeight = resolveSize(calculateHeight, heightMeasureSpec)

        setMeasuredDimension(resolveWidth, resolveHeight)
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val maxXPos = measuredWidth

        var cursorY = 0
        var cursorX = 0

        var maxHeightRow = 0

        children.forEach { child ->
            //#region Checking New Line
            val nextWidth = cursorX + child.measuredWidth
            if (nextWidth > maxXPos) {
                cursorX = 0
                cursorY += maxHeightRow + verticalSpace
                maxHeightRow = 0
            }
            //#endregion

            //#region Draw Child
            val rectChild = Rect()

            rectChild.top = cursorY
            rectChild.left = cursorX
            rectChild.bottom = rectChild.top + child.measuredHeight
            rectChild.right = rectChild.left + child.measuredWidth

            child.layout(rectChild)
            //#endregion

            //#region Calculate Next Position
            cursorX += child.measuredWidth + horizontalSpace
            if (child.measuredHeight > maxHeightRow) maxHeightRow = child.measuredHeight
            //#endregion
        }
    }
}