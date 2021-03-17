package ru.fasdev.tfs.view.ui.global.view.layout

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import androidx.core.view.children
import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.feature.util.layout
import ru.fasdev.tfs.view.feature.util.toDp

class FlexBoxLayout
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {
    companion object {
        private val DEFAULT_VERTICAL_SPACE = 7.toDp
        private val DEFAULT_HORIZONTAL_SPACE = 10.toDp
        private const val DEFAULT_GRAVITY = Gravity.LEFT
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

    var gravity = DEFAULT_GRAVITY
        set(value) {
            if (field != value) {
                field = value
                invalidate()
            }
        }

    init {
        setWillNotDraw(false)

        context.obtainStyledAttributes(attrs, R.styleable.FlexBoxLayout).apply {
            verticalSpace = getDimension(
                R.styleable.FlexBoxLayout_android_verticalSpacing,
                DEFAULT_VERTICAL_SPACE.toFloat()
            ).toInt()

            horizontalSpace = getDimension(
                R.styleable.FlexBoxLayout_android_horizontalSpacing,
                DEFAULT_HORIZONTAL_SPACE.toFloat()
            ).toInt()

            gravity = getInt(R.styleable.FlexBoxLayout_android_gravity, DEFAULT_GRAVITY)

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
            measureChild(child, widthMeasureSpec, heightMeasureSpec)

            // #region Checking New Line
            val nexWidth = cursorX + child.measuredWidth
            if (nexWidth > maxWidth) {
                cursorX = 0
                cursorY += maxHeightRow + verticalSpace
                maxHeightRow = 0
            }
            // #endregion

            // #region Calculate Next Position
            cursorX += child.measuredWidth + horizontalSpace

            if (nexWidth > maxWidthRow) maxWidthRow = nexWidth
            if (child.measuredHeight > maxHeightRow) maxHeightRow = child.measuredHeight
            // #endregion
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
        val isLeft = gravity == Gravity.LEFT

        var cursorY = 0
        var cursorX = if (isLeft) 0 else measuredWidth

        var maxHeightRow = 0

        // TODO: FIXED gravity draw
        children.forEach { child ->
            // #region Checking New Line
            val nextWidth = if (isLeft) cursorX + child.measuredWidth else cursorX - child.measuredWidth
            val isNextWidth = if (isLeft) nextWidth > maxXPos else nextWidth < 0

            if (isNextWidth) {
                cursorX = if (isLeft) 0 else measuredWidth

                cursorY += maxHeightRow + verticalSpace
                maxHeightRow = 0
            }
            // #endregion

            // #region Draw Child
            val rectChild = Rect()

            rectChild.top = cursorY
            rectChild.bottom = rectChild.top + child.measuredHeight

            if (isLeft) {
                rectChild.left = cursorX
                rectChild.right = rectChild.left + child.measuredWidth
            } else {
                rectChild.right = cursorX
                rectChild.left = rectChild.right - child.measuredWidth
            }

            child.layout(rectChild)
            // #endregion

            // #region Calculate Next Position
            if (isLeft) cursorX += child.measuredWidth + horizontalSpace
            else cursorX -= child.measuredWidth + horizontalSpace
            if (child.measuredHeight > maxHeightRow) maxHeightRow = child.measuredHeight
            // #endregion
        }
    }
}
