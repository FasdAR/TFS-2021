package ru.fasdev.tfs.fragmentRouter

import androidx.fragment.app.Fragment

interface FragmentRouter {
    fun navigateTo(fragmentScreen: FragmentScreen)
    fun replaceTo(fragmentScreen: FragmentScreen)
    fun getCurrentFragment(): Fragment?
    fun back()
}
