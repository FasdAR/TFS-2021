package ru.fasdev.tfs.view.ui.fragment.chat.adapter.viewTypes

import ru.fasdev.tfs.R
import ru.fasdev.tfs.view.feature.customView.viewGroup.message.model.MessageReactionUi
import ru.fasdev.tfs.view.feature.recycler.base.ViewTyped

data class InternalMessageUi(override val uId: Int,
                        override val message: String,
                        override val reactions: List<MessageReactionUi>,
                        override val viewType: Int = R.layout.item_internal_message): MessageUi()