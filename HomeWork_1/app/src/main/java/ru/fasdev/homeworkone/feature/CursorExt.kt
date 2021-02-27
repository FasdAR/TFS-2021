package ru.fasdev.homeworkone.feature

import android.database.Cursor

fun Cursor?.wrapUse(block: (Cursor) -> Unit) {
    this?.let { block(it) }
}