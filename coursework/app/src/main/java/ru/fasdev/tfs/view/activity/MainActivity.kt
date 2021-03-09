package ru.fasdev.tfs.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.customView.MessageView
import ru.fasdev.tfs.view.customView.ReactionView
import ru.fasdev.tfs.view.model.ReactionUiModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<MessageView>(R.id.msg_view).reactionList = arrayListOf(
                ReactionUiModel("\uD83D\uDE02", 2),
                ReactionUiModel("\uD83D\uDE02", 2, isSelected = true),
                ReactionUiModel("\uD83D\uDE02", 2),
                ReactionUiModel("\uD83D\uDE02", 2),
                ReactionUiModel("\uD83D\uDE02", 2, isSelected = true),
                ReactionUiModel("\uD83D\uDE02", 2),
                ReactionUiModel("\uD83D\uDE02", 2),
                ReactionUiModel("\uD83D\uDE02", 2, isSelected = true),
                ReactionUiModel("\uD83D\uDE02", 2)
        )
    }
}