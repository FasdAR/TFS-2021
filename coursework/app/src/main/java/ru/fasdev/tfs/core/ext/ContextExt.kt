package ru.fasdev.tfs.core.ext

import android.content.Context
import android.content.res.Resources
import androidx.annotation.Dimension
import androidx.core.content.ContextCompat
import kotlin.math.roundToInt

@get:Dimension(unit = Dimension.DP)
val Int.toDp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()

@get:Dimension(unit = Dimension.SP)
val Int.toSp: Int
    get() = (this * Resources.getSystem().displayMetrics.scaledDensity).roundToInt()

@get:Dimension(unit = Dimension.DP)
val Float.toDp: Float
    get() = (this * Resources.getSystem().displayMetrics.density)

@get:Dimension(unit = Dimension.SP)
val Float.toSp: Float
    get() = (this * Resources.getSystem().displayMetrics.scaledDensity)

fun Context.getColorCompat(resId: Int) = ContextCompat.getColor(this, resId)
