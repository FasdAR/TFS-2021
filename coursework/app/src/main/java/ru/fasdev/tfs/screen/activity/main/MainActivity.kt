package ru.fasdev.tfs.screen.activity.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import ru.fasdev.tfs.R
import ru.fasdev.tfs.core.ext.initEdgeToEdge
import ru.fasdev.tfs.di.provide.ProvideFragmentRouter
import ru.fasdev.tfs.fragmentRouter.FragmentRouter
import ru.fasdev.tfs.fragmentRouter.OnBackPressedListener
import ru.fasdev.tfs.fragmentRouter.base.BaseFragmentRouter
import javax.inject.Inject

class MainActivity : AppCompatActivity(), ProvideFragmentRouter, HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    private val fragmentRouter: FragmentRouter by lazy {
        BaseFragmentRouter(supportFragmentManager, R.id.main_container)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initEdgeToEdge()
    }

    override fun getRouter(): FragmentRouter = fragmentRouter

    override fun onBackPressed() {
        val fragment = fragmentRouter.getCurrentFragment()

        if (fragment is OnBackPressedListener) {
            if (fragment.onBackPressed()) return
        }

        super.onBackPressed()
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector
}
