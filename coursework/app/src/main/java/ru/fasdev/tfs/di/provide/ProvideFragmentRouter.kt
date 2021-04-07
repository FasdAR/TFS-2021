package ru.fasdev.tfs.di.provide

import ru.fasdev.tfs.fragmentRouter.FragmentRouter

interface ProvideFragmentRouter {
    fun getRouter(): FragmentRouter
}
