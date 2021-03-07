package ru.fasdev.tfs.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.customView.ReactionView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<ReactionView>(R.id.reaction_view).setOnClickListener {
            (it as ReactionView).clickReaction()
        }
    }
}