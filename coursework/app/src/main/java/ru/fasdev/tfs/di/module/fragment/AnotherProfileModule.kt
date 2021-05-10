package ru.fasdev.tfs.di.module.fragment

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import ru.fasdev.tfs.di.key.ViewModelKey
import ru.fasdev.tfs.di.scope.FragmentScope
import ru.fasdev.tfs.screen.fragment.anotherProfile.AnotherProfileFragment
import ru.fasdev.tfs.screen.fragment.anotherProfile.AnotherProfileViewModel

@Module
abstract class AnotherProfileModule
{
    @FragmentScope
    @ContributesAndroidInjector(modules = [])
    abstract fun createFragment(): AnotherProfileFragment

    @Binds
    @IntoMap
    @ViewModelKey(AnotherProfileViewModel::class)
    abstract fun createViewModel(viewModel: AnotherProfileViewModel): ViewModel
}