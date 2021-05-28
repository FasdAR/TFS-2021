package ru.fasdev.tfs

import android.app.Application
import android.util.Log
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import io.reactivex.plugins.RxJavaPlugins
import ru.fasdev.tfs.di.component.DaggerAppComponent
import javax.inject.Inject

class TfsApp : Application(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent
            .factory()
            .create(this)
            .inject(this)

        RxJavaPlugins.setErrorHandler { e: Throwable? -> Log.e("RxDisposeError", e?.message.toString()) }
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector
}
