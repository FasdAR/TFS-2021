package ru.fasdev.tfs.view.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.ui.custom.view.ReactionView
import ru.fasdev.tfs.view.ui.custom.viewGroup.message.MessageViewGroup
import ru.fasdev.tfs.view.ui.custom.viewGroup.message.model.MessageReactionUi

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
