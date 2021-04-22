package ru.fasdev.tfs.view.ui.global.view.layout

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.feature.util.layout
import ru.fasdev.tfs.view.feature.util.toDp

class FlexBoxLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {
    private companion object {
        val DEFAULT_VERTICAL_SPACE = 7.toDp
        val DEFAULT_HORIZONTAL_SPACE = 10.toDp
        const val DEFAULT_GRAVITY = Gravity.START
    }

    private var verticalSpace = DEFAULT_VERTICAL_SPACE
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    private var horizontalSpace = DEFAULT_HORIZONTAL_SPACE
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

    private val childrenRows: MutableList<List<View>> = mutableListOf()

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

    @ExperimentalStdlibApi
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        childrenRows.clear()
        val maxWidth = MeasureSpec.getSize(widthMeasureSpec)

        var cursorY = 0
        var cursorX = 0

        var maxHeightRow = 0
        var maxWidthRow = 0

        val childrenRow: MutableList<View> = mutableListOf()

        children.forEach { child ->
            measureChild(child, widthMeasureSpec, heightMeasureSpec)

            // #region Checking New Line
            val nexWidth = cursorX + child.measuredWidth
            if (nexWidth > maxWidth) {
                childrenRows.add(buildList { addAll(childrenRow) })
                childrenRow.clear()
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

            childrenRow.add(child)
        }

        if (childrenRow.isNotEmpty())
            childrenRows.add(childrenRow)

        val calculateWidth = maxWidthRow
        val calculateHeight = cursorY + maxHeightRow

        val resolveWidth = resolveSize(calculateWidth, widthMeasureSpec)
        val resolveHeight = resolveSize(calculateHeight, heightMeasureSpec)

        setMeasuredDimension(resolveWidth, resolveHeight)
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val maxXPos = measuredWidth
        val isStart = gravity == Gravity.START

        var cursorY = 0
        var cursorX = if (isStart) 0 else maxXPos

        var maxHeightRow = 0

        childrenRows.forEach { childRow ->
            val newRows = if (isStart) childRow else childRow.reversed()
            newRows.forEach { child ->
                val rectChild = Rect()

                rectChild.top = cursorY
                rectChild.bottom = rectChild.top + child.measuredHeight

                if (isStart) {
                    rectChild.left = cursorX
                    rectChild.right = rectChild.left + child.measuredWidth

                    cursorX += child.measuredWidth + horizontalSpace
                } else {
                    rectChild.right = cursorX
                    rectChild.left = rectChild.right - child.measuredWidth

                    cursorX -= child.measuredWidth + horizontalSpace
                }

                child.layout(rectChild)

                if (child.measuredHeight > maxHeightRow) maxHeightRow = child.measuredHeight
            }

            cursorX = if (isStart) 0 else maxXPos
            cursorY += maxHeightRow + verticalSpace
            maxHeightRow = 0
        }
    }
}
