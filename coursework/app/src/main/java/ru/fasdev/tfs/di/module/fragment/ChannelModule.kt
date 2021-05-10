package ru.fasdev.tfs.di.module.fragment

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.fasdev.tfs.di.scope.ContainerFragmentScope
import ru.fasdev.tfs.screen.fragment.channels.ChannelsFragment
import ru.fasdev.tfs.screen.fragment.main.MainFragment

@Module
abstract class ChannelModule
{
    @ContainerFragmentScope
    @ContributesAndroidInjector(modules = [])
    abstract fun createFragment(): ChannelsFragment
}