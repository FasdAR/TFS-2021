package ru.fasdev.tfs.view.ui.global.fragmentRouter

import androidx.fragment.app.Fragment

interface FragmentRouter
{
    fun navigateTo(fragmentScreen: FragmentScreen)
    fun replaceTo(fragmentScreen: FragmentScreen)
    fun getCurrentFragment(): Fragment?
    fun back()
}