package ru.fasdev.tfs.view.feature.util

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun View.layout(rect: Rect) {
    layout(rect.left, rect.top, rect.right, rect.bottom)
}

//#region Get size margin
fun View.getWidthMeasuredMargin(): Int {
    val layoutParams = layoutParams as ViewGroup.MarginLayoutParams
    return measuredWidth + layoutParams.leftMargin + layoutParams.rightMargin
}

fun View.getHeightMeasuredMargin(): Int {
    val layoutParams = layoutParams as ViewGroup.MarginLayoutParams
    return measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin
}
//#ednregion

//#region Keyboard
fun EditText.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

    requestFocus()
}

fun EditText.hideKeyboard(isClearContent: Boolean = true) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)

    if (isClearContent) {
        text.clear()
    }

    clearFocus()
}
//#endregion
