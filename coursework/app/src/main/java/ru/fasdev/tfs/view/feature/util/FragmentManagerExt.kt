package ru.fasdev.tfs.view.feature.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

fun FragmentManager.replaceCommit(idContainer: Int, fragment: Fragment, tag: String) {
    beginTransaction().replace(idContainer, fragment, tag).commit()
}

fun FragmentManager.getCurrentFragment(idContainer: Int): Fragment? = findFragmentById(idContainer)