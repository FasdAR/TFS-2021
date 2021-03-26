package ru.fasdev.tfs.view.ui.activity.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.feature.util.initEdgeToEdge
import ru.fasdev.tfs.view.ui.global.fragmentRouter.BaseFragmentRouter
import ru.fasdev.tfs.view.ui.global.fragmentRouter.FragmentRouter
import ru.fasdev.tfs.view.ui.global.fragmentRouter.ProvideFragmentRouter

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
}
