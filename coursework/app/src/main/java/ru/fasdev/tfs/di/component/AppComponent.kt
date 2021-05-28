package ru.fasdev.tfs.di.component

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import ru.fasdev.tfs.TfsApp
import ru.fasdev.tfs.di.module.AppModule
import ru.fasdev.tfs.di.scope.AppScope

@AppScope
@Component(modules = [AndroidInjectionModule::class, AppModule::class])
interface AppComponent : AndroidInjector<TfsApp> {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: TfsApp): AppComponent
    }

    override fun inject(instance: TfsApp?)
}
