package ru.fasdev.tfs.view.ui.activity.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.feature.util.replaceCommit
import ru.fasdev.tfs.view.feature.util.replaceCommitTransaction
import ru.fasdev.tfs.view.ui.global.fragmentRouter.FragmentRouter

class MainActivity : AppCompatActivity(), FragmentRouter {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun navigateTo(fragment: Fragment, tag: String) {
        supportFragmentManager
                .replaceCommitTransaction(R.id.main_container, fragment, tag)
                .addToBackStack(null)
                .commit()
    }

    override fun back() {
        supportFragmentManager.popBackStack()
    }
}
