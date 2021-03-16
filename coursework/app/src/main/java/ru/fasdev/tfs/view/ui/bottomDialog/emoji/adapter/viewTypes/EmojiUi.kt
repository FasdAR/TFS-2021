package ru.fasdev.tfs.view.ui.bottomDialog.emoji.adapter.viewTypes

import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.feature.recycler.base.ViewTyped

class EmojiUi(override val uId: Int, val emoji: String,
              override val viewType: Int = R.layout.item_emoji): ViewTyped()