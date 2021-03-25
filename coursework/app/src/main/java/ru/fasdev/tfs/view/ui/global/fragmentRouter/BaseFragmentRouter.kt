package ru.fasdev.tfs.view.ui.global.fragmentRouter

import androidx.fragment.app.FragmentManager
import ru.fasdev.tfs.view.feature.util.getCurrentFragment

class BaseFragmentRouter(private val fragmentManager: FragmentManager, private val idContainer: Int): FragmentRouter
{
    override fun navigateTo(fragmentScreen: FragmentScreen) {
        fragmentManager.beginTransaction()
            .apply {
                add(idContainer, fragmentScreen.fragment, fragmentScreen.tag)
                fragmentManager.getCurrentFragment(idContainer)?.let { hide(it) }
                addToBackStack(fragmentScreen.tag)
            }.commit()
    }

    override fun back() {
        fragmentManager.popBackStack()
    }
}