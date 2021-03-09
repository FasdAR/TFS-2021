package ru.fasdev.tfs.view.feature.util

import android.content.res.Resources
import kotlin.math.roundToInt

val Int.toDp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()

val Int.toSp: Int
    get() = (this * Resources.getSystem().displayMetrics.scaledDensity).roundToInt()

val Float.toDp: Float
    get() = (this * Resources.getSystem().displayMetrics.density)

val Float.toSp: Float
    get() = (this * Resources.getSystem().displayMetrics.scaledDensity)
