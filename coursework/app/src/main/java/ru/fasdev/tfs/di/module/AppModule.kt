package ru.fasdev.tfs.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.fasdev.tfs.TfsApp
import ru.fasdev.tfs.di.module.activity.MainActivityModule
import ru.fasdev.tfs.di.module.data.RepositoryModule
import ru.fasdev.tfs.di.module.data.RetrofitModule
import ru.fasdev.tfs.di.module.data.RoomModule

@Module(includes = [ActivityModule::class, RetrofitModule::class, RoomModule::class, RepositoryModule::class])
class AppModule {
    @Provides
    fun provideAppContext(app: TfsApp): Context {
        return app
    }
}

@Module(includes = [MainActivityModule::class])
abstract class ActivityModule