package ru.fasdev.homeworkone.feature

import android.database.Cursor
import androidx.core.database.getStringOrNull

fun Cursor?.wrapUse(block: (Cursor) -> Unit) {
    this?.let { block(it) }
}

fun Cursor.getValue(column: String): String = getString(getColumnIndex(column))
fun Cursor.getValueOrNull(column: String): String? = getStringOrNull(getColumnIndex(column))