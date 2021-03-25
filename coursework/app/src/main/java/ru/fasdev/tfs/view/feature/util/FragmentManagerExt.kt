package ru.fasdev.tfs.view.feature.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

fun FragmentManager.getCurrentFragment(idContainer: Int): Fragment? = findFragmentById(idContainer)