package ru.fasdev.tfs.core.ext

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

fun FragmentManager.getCurrentFragment(idContainer: Int): Fragment? = findFragmentById(idContainer)
