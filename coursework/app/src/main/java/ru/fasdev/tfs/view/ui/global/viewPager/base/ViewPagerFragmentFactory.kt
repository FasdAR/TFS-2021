package ru.fasdev.tfs.view.ui.global.viewPager.base

import androidx.fragment.app.Fragment

interface ViewPagerFragmentFactory
{
    fun createFragment(position: Int): Fragment?
    fun getSize(): Int
}