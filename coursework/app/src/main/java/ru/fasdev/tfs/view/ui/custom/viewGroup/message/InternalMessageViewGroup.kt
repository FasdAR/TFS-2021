package ru.fasdev.tfs.view.ui.custom.viewGroup.message

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.ui.custom.layout.FlexboxLayout

class InternalMessageViewGroup
    @JvmOverloads constructor(
            context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0,
            defStyleRes: Int = 0
    ) : SimpleMessageViewGroup(context, attrs, defStyleAttr, defStyleRes)
{
    override val msgTextView: TextView
    override val reactionLayout: FlexboxLayout

    init {
        LayoutInflater.from(context).inflate(R.layout.view_internal_message, this, true)

        msgTextView = findViewById(R.id.message_text)
        reactionLayout = findViewById(R.id.reaction_layout)

        context.obtainStyledAttributes(attrs, R.styleable.InternalMessageViewGroup).apply {
            msgText = getString(R.styleable.InternalMessageViewGroup_msgText) ?: ""
            msgViewMaxSize = getMsgViewMaxSize()

            recycle()
        }

        updateReactionLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int)
    {
        TODO("Not yet implemented")
    }

    override fun generateDefaultLayoutParams() = MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    override fun generateLayoutParams(attrs: AttributeSet) = MarginLayoutParams(context, attrs)
    override fun generateLayoutParams(p: LayoutParams) = MarginLayoutParams(p)
}