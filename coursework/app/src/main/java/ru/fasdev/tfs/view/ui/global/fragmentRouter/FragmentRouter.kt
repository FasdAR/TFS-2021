package ru.fasdev.tfs.view.ui.global.fragmentRouter

interface FragmentRouter
{
    fun navigateTo(fragmentScreen: FragmentScreen)
    fun replaceTo(fragmentScreen: FragmentScreen)
    fun back()
}