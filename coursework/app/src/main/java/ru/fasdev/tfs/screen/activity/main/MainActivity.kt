package ru.fasdev.tfs.screen.activity.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.fasdev.tfs.R
import ru.fasdev.tfs.di.provide.ProvideFragmentRouter
import ru.fasdev.tfs.core.ext.initEdgeToEdge
import ru.fasdev.tfs.fragmentRouter.FragmentRouter
import ru.fasdev.tfs.fragmentRouter.ProviderBackPressed
import ru.fasdev.tfs.fragmentRouter.base.BaseFragmentRouter

class MainActivity : AppCompatActivity(), ProvideFragmentRouter {
    private val fragmentRouter: FragmentRouter by lazy {
        BaseFragmentRouter(supportFragmentManager, R.id.main_container)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initEdgeToEdge()
    }

    override fun getRouter(): FragmentRouter = fragmentRouter

    override fun onBackPressed() {
        val fragment = fragmentRouter.getCurrentFragment()

        if (fragment is ProviderBackPressed) {
            if (fragment.onBackPressed()) return
        }

        super.onBackPressed()
    }
}
