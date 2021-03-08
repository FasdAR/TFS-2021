package ru.fasdev.tfs.view.customView

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.children

class FlexBoxLayout
    @JvmOverloads
    constructor(
            context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0,
            defStyleRes: Int = 0
    ): ViewGroup(context, attrs, defStyleAttr, defStyleRes)
{
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        children.forEach {
            measureChildWithMargins(it, widthMeasureSpec, 0, heightMeasureSpec, 0)
        }
    }
    
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        children.forEach {

        }
    }
}