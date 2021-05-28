package ru.fasdev.tfs.di.module.fragment

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import ru.fasdev.tfs.di.key.ViewModelKey
import ru.fasdev.tfs.di.scope.FragmentScope
import ru.fasdev.tfs.screen.fragment.streamList.StreamListFragment
import ru.fasdev.tfs.screen.fragment.streamList.StreamListViewModel

@Module
abstract class StreamListModule {
    @FragmentScope
    @ContributesAndroidInjector(modules = [])
    abstract fun createFragment(): StreamListFragment

    @Binds
    @IntoMap
    @ViewModelKey(StreamListViewModel::class)
    abstract fun createViewModel(viewModel: StreamListViewModel): ViewModel
}
