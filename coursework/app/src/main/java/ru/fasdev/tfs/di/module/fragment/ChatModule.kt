package ru.fasdev.tfs.di.module.fragment

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import ru.fasdev.tfs.di.key.ViewModelKey
import ru.fasdev.tfs.di.scope.FragmentScope
import ru.fasdev.tfs.screen.fragment.chat.ChatFragment
import ru.fasdev.tfs.screen.fragment.chat.ChatViewModel

@Module
abstract class ChatModule {
    @FragmentScope
    @ContributesAndroidInjector(modules = [])
    abstract fun createFragment(): ChatFragment

    @Binds
    @IntoMap
    @ViewModelKey(ChatViewModel::class)
    abstract fun createViewModel(viewModel: ChatViewModel): ViewModel
}
