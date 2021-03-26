package ru.fasdev.tfs.view.feature.util

import android.graphics.Insets
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.WindowInsets
import androidx.core.view.WindowCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.FragmentActivity

// Включает full screen
fun FragmentActivity.initEdgeToEdge(insetsView: (() -> Unit)? = null) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        window?.run {
            WindowCompat.setDecorFitsSystemWindows(this, false)
        }
        insetsView?.invoke()
    }
}

// Устанавливает padding для View под System bar
fun View.setSystemInsetsInTop() {
    doOnApplyWindowsInsets { view, windowInsets, initialPadding ->
        val systemInsets = windowInsets.getSystemInsets()
        view.updatePadding(top = initialPadding.top + systemInsets.top)
    }
}

// Обработчик для удобной работы с отступами,
// сохраняет актуальные паддинги и отдает необходимую view
fun View.doOnApplyWindowsInsets(block: (View, WindowInsets, Rect) -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val padding = recordPadding()

        setOnApplyWindowInsetsListener { v, insets ->
            block(v, insets, padding)
            insets
        }

        requestApplyInsetsWhenAttached()
    }
}

// Отдает системные отступы
fun WindowInsets.getSystemInsets(): Insets {
    return when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
            getInsets(WindowInsets.Type.systemBars())
        }
        Build.VERSION.SDK_INT == Build.VERSION_CODES.Q -> {
            systemWindowInsets
        }
        else -> {
            throw Exception("Don't support type insets")
        }
    }
}

// Записывает отступы
private fun View.recordPadding(): Rect {
    return Rect(paddingLeft, paddingTop, paddingRight, paddingBottom)
}

// Запрашивает повторные отсупы
private fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        requestApplyInsets()
    } else {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View?) {
                v?.removeOnAttachStateChangeListener(this)
                v?.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View?) = Unit
        })
    }
}
