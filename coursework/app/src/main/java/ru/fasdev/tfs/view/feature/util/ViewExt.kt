package ru.fasdev.tfs.view.feature.util

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup

fun View.layout(rect: Rect) {
    layout(rect.left, rect.top, rect.right, rect.bottom)
}

fun View.getWidthMeasuredMargin(): Int {
    val layoutParams = layoutParams as ViewGroup.MarginLayoutParams

    return measuredWidth + layoutParams.leftMargin + layoutParams.rightMargin
}

fun View.getHeightMeasuredMargin(): Int {
    val layoutParams = layoutParams as ViewGroup.MarginLayoutParams

    return measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin
}