package ru.fasdev.tfs.view.ui.global.fragmentRouter

import androidx.fragment.app.Fragment

interface FragmentRouter
{
    fun navigateTo(fragment: Fragment, tag: String)
    fun back()
}