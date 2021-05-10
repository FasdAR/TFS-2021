package ru.fasdev.tfs.di.module.fragment

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import ru.fasdev.tfs.di.key.ViewModelKey
import ru.fasdev.tfs.di.scope.FragmentScope
import ru.fasdev.tfs.screen.fragment.people.PeopleFragment
import ru.fasdev.tfs.screen.fragment.people.PeopleViewModel

@Module
abstract class PeopleModule {
    @FragmentScope
    @ContributesAndroidInjector(modules = [])
    abstract fun createFragment(): PeopleFragment

    @Binds
    @IntoMap
    @ViewModelKey(PeopleViewModel::class)
    abstract fun createViewModel(viewModel: PeopleViewModel): ViewModel
}
