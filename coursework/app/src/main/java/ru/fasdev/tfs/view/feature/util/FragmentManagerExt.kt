package ru.fasdev.tfs.view.feature.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

fun FragmentManager.replaceCommit(idContainer: Int, fragment: Fragment, tag: String) {
    beginTransaction().replace(idContainer, fragment, tag).commit()
}

fun FragmentManager.replaceCommitTransaction(idContainer: Int, fragment: Fragment, tag: String): FragmentTransaction {
    return beginTransaction().replace(idContainer, fragment, tag)
}

fun FragmentManager.getCurrentFragment(idContainer: Int): Fragment? = findFragmentById(idContainer)