package ru.fasdev.tfs.fragmentRouter.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import ru.fasdev.tfs.R
import ru.fasdev.tfs.core.ext.getCurrentFragment
import ru.fasdev.tfs.fragmentRouter.FragmentRouter
import ru.fasdev.tfs.fragmentRouter.FragmentScreen

class BaseFragmentRouter(
    private val fragmentManager: FragmentManager,
    private val idContainer: Int
) : FragmentRouter {
    override fun navigateTo(fragmentScreen: FragmentScreen) {
        fragmentManager.commit {
            setCustomAnimations(R.anim.fade_out, R.anim.fade_in, R.anim.fade_out, R.anim.fade_in)
            add(idContainer, fragmentScreen.fragment, fragmentScreen.tag)
            fragmentManager.getCurrentFragment(idContainer)?.let { hide(it) }
            addToBackStack(fragmentScreen.tag)
        }
    }

    override fun replaceTo(fragmentScreen: FragmentScreen) {
        fragmentManager.commit {
            setCustomAnimations(R.anim.fade_out, R.anim.fade_in)
            replace(idContainer, fragmentScreen.fragment, fragmentScreen.tag)
        }
    }

    override fun getCurrentFragment(): Fragment? = fragmentManager.getCurrentFragment(idContainer)

    override fun back() {
        fragmentManager.popBackStack()
    }
}
