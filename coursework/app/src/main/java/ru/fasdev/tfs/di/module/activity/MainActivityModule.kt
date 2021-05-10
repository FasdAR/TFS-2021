package ru.fasdev.tfs.di.module.activity

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.fasdev.tfs.di.module.fragment.*
import ru.fasdev.tfs.di.module.viewModel.ViewModelModule
import ru.fasdev.tfs.di.scope.ActivityScope
import ru.fasdev.tfs.screen.activity.main.MainActivity

@Module
abstract class MainActivityModule
{
    @ActivityScope
    @ContributesAndroidInjector(
        modules = [ViewModelModule::class, AnotherProfileModule::class, ChatModule::class, MainModule::class, OwnProfileModule::class, PeopleModule::class, StreamListModule::class, ChannelModule::class]
    )
    abstract fun createMainActivityModule(): MainActivity
}