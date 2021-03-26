package ru.fasdev.tfs.view.feature.util

import android.graphics.Insets
import android.os.Build
import android.view.View
import android.view.WindowInsets
import androidx.appcompat.widget.Toolbar
import androidx.core.view.WindowCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.initEdgeToEdge(insetsView: (() -> Unit)? = null) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        window?.run {
            WindowCompat.setDecorFitsSystemWindows(this, false)
        }
        insetsView?.invoke()
    }
}

fun View.setSystemInsetsInTop() {
    doOnApplyWindowsInsets { view, windowInsets, initialPadding ->
        val systemInsets = windowInsets.getSystemInsets()
        view.updatePadding(top = initialPadding.top + systemInsets.top)
    }
}

fun View.setSystemInsets() {
    rootView.doOnApplyWindowsInsets { view, windowInsets, _ ->
        val systemInsets = windowInsets.getSystemInsets()
        view.updatePadding(
                top = systemInsets.top,
                left = systemInsets.left,
                right = systemInsets.right,
                bottom = systemInsets.bottom
        )
    }
}

fun View.doOnApplyWindowsInsets(block: (View, WindowInsets, InitialPadding) -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val padding = recordPadding()

        setOnApplyWindowInsetsListener { v, insets ->
            block(v, insets, padding)
            insets
        }

        requestApplyInsetsWhenAttached()
    }
}

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

data class InitialPadding(val left: Int, val top: Int, val right: Int, val bottom: Int)

private fun View.recordPadding(): InitialPadding =
        InitialPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)

fun View.requestApplyInsetsWhenAttached() {
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