package ru.fasdev.tfs.view.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.ui.custom.viewGroup.message.MessageViewGroup
import ru.fasdev.tfs.view.ui.custom.view.ReactionView
import ru.fasdev.tfs.view.ui.custom.viewGroup.message.model.MessageReactionUi

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val msgView = findViewById<MessageViewGroup>(R.id.msg_view)

        msgView.onClickReactionListener = object : MessageViewGroup.OnClickReactionListener {
            override fun onClick(reactionView: ReactionView) {
                reactionView.selectedReaction()
            }
        }

        msgView.reactionList = arrayListOf(
                MessageReactionUi("\uD83D\uDE00", 2),
                MessageReactionUi("\uD83D\uDE03", 2, isSelected = true),
                MessageReactionUi("\uD83E\uDD76", 2),
                MessageReactionUi("\uD83D\uDE1D", 2),
                MessageReactionUi("\uD83D\uDE0A", 2, isSelected = true),
                MessageReactionUi("\uD83E\uDD2D", 2),
                MessageReactionUi("\uD83E\uDD2C", 2),
                MessageReactionUi("\uD83E\uDD22", 2, isSelected = true),
                MessageReactionUi("\uD83D\uDE44", 2)
        )
    }
}