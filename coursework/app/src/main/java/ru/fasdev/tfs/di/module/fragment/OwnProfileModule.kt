package ru.fasdev.tfs.di.module.fragment

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import ru.fasdev.tfs.di.key.ViewModelKey
import ru.fasdev.tfs.di.scope.FragmentScope
import ru.fasdev.tfs.screen.fragment.ownProfile.OwnProfileFragment
import ru.fasdev.tfs.screen.fragment.ownProfile.OwnProfileViewModel

@Module
abstract class OwnProfileModule {
    @FragmentScope
    @ContributesAndroidInjector(modules = [])
    abstract fun createFragment(): OwnProfileFragment

    @Binds
    @IntoMap
    @ViewModelKey(OwnProfileViewModel::class)
    abstract fun createViewModel(viewModel: OwnProfileViewModel): ViewModel
}