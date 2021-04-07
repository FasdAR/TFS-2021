package ru.fasdev.tfs.di

import ru.fasdev.tfs.fragmentRouter.FragmentRouter

interface ProvideFragmentRouter {
    fun getRouter(): FragmentRouter
}
