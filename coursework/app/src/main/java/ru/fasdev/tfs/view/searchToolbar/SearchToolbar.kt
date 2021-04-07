package ru.fasdev.tfs.view.searchToolbar

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.core.widget.addTextChangedListener
import ru.fasdev.tfs.R
import ru.fasdev.tfs.old.view.feature.util.hideKeyboard
import ru.fasdev.tfs.old.view.feature.util.showKeyboard
import ru.fasdev.tfs.core.ext.toDp

class SearchToolbar @JvmOverloads
constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) :
    LinearLayout(context, attributeSet, defStyleAttr, defStyleRes) {
    fun interface TextChangeListener {
        fun newText(query: String)
    }

    private val backBtn: ImageButton
    private val editText: AppCompatEditText

    val isSearch get() = isVisible

    var attachToolbar: View? = null
    var textChangeListener: TextChangeListener? = null

    init {
        LayoutInflater.from(context).inflate(
            R.layout.layout_toolbar_search,
            this, true
        )

        backBtn = findViewById(R.id.btn_back_search)
        editText = findViewById(R.id.search_edt)

        backBtn.setOnClickListener {
            closeSearch()
        }

        editText.addTextChangedListener {
            textChangeListener?.newText(it.toString())
        }

        gravity = Gravity.CENTER
        updatePadding(left = 22.toDp, right = 19.toDp)
        setBackgroundColor(ContextCompat.getColor(context, R.color.grey_910))
    }

    fun hideKeyboard() {
        editText.hideKeyboard(false)
    }

    fun openSearch() {
        attachToolbar?.visibility = INVISIBLE
        isVisible = true
        editText.showKeyboard()
    }

    fun closeSearch() {
        if (isSearch) {
            attachToolbar?.visibility = VISIBLE
            isVisible = false
            editText.hideKeyboard()
        }
    }
}
