package ru.fasdev.tfs.view.customView

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.util.getHeightMeasuredMargin
import ru.fasdev.tfs.view.util.getWidthMeasuredMargin
import ru.fasdev.tfs.view.util.layout

class MessageView
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
    ) : ViewGroup(context, attrs, defStyleAttr, defStyleRes)
{
    private val avatarImageView: ImageView
    private val msgLayout: ViewGroup

    private val avatarLayoutParams: MarginLayoutParams
        get() = avatarImageView.layoutParams as MarginLayoutParams

    private val msgLayoutParams: MarginLayoutParams
        get() = msgLayout.layoutParams as MarginLayoutParams

    private val avatarRect = Rect()
    private val msgRect = Rect()

    init {
        LayoutInflater.from(context).inflate(R.layout.message_view, this, true)

        avatarImageView = findViewById(R.id.avatar)
        msgLayout = findViewById(R.id.msg_layout)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //#region Avatar Size
        measureChildWithMargins(avatarImageView, widthMeasureSpec, 0,
                heightMeasureSpec, 0)

        val avatarHeight = avatarImageView.getHeightMeasuredMargin()
        val avatarWidth = avatarImageView.getWidthMeasuredMargin()
        //#endregion

        //#region MsgLayout Size
        measureChildWithMargins(msgLayout, widthMeasureSpec, avatarWidth,
                heightMeasureSpec, 0)

        val msgHeight = msgLayout.getHeightMeasuredMargin()
        val msgWidth = msgLayout.getWidthMeasuredMargin()
        //#endregion

        val resolveWidth = resolveSize(avatarWidth + msgWidth, widthMeasureSpec)
        val resolveHeight = resolveSize(maxOf(avatarHeight, msgHeight), heightMeasureSpec)

        setMeasuredDimension(resolveWidth, resolveHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        //#region Avatar Calculate Size
        avatarRect.left = avatarLayoutParams.leftMargin
        avatarRect.top = avatarLayoutParams.topMargin
        avatarRect.right = avatarRect.left + avatarImageView.measuredWidth
        avatarRect.bottom = avatarRect.top + avatarImageView.measuredHeight
        //#endregion

        //#region Msg Calculate Size
        msgRect.left = avatarImageView.getWidthMeasuredMargin() + msgLayoutParams.leftMargin
        msgRect.top = msgLayoutParams.topMargin
        msgRect.right = msgRect.left + msgLayout.measuredWidth
        msgRect.bottom = msgRect.top + msgLayout.measuredHeight
        //#endregion

        avatarImageView.layout(avatarRect)
        msgLayout.layout(msgRect)
    }

    override fun generateDefaultLayoutParams() = MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT)
    override fun generateLayoutParams(attrs: AttributeSet) = MarginLayoutParams(context, attrs)
    override fun generateLayoutParams(p: LayoutParams) = MarginLayoutParams(p)
}