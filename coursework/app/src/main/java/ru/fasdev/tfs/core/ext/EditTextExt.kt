package ru.fasdev.tfs.core.ext

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

// #region Keyboard
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
// #endregion