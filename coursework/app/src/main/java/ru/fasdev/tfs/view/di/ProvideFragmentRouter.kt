package ru.fasdev.tfs.view.di

import ru.fasdev.tfs.view.ui.global.fragmentRouter.FragmentRouter

interface ProvideFragmentRouter {
    fun getRouter(): FragmentRouter
}
