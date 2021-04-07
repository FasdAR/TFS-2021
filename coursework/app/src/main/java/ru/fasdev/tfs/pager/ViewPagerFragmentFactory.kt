package ru.fasdev.tfs.pager

import android.content.Context
import androidx.fragment.app.Fragment

interface ViewPagerFragmentFactory {
    fun createFragment(position: Int): Fragment?
    fun getTitle(context: Context, position: Int): String
    fun getSize(): Int
}
